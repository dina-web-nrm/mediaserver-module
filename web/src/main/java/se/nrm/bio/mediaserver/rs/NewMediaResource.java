package se.nrm.bio.mediaserver.rs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.*;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Attachment;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.ListWrapper;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.MediaText;
import se.nrm.bio.mediaserver.domain.MetadataHeader;
import se.nrm.bio.mediaserver.domain.Sound;
import se.nrm.bio.mediaserver.domain.Wrapper;
import se.nrm.bio.mediaserver.domain.Video;
import se.nrm.mediaserver.resteasy.util.PathHelper;

/**
 *
 * @author ingimar
 */
@Path("")
public class NewMediaResource {

    private final static Logger logger = Logger.getLogger(MediaResource.class);

    @EJB
    private MediaserviceBean service;

    @EJB
    protected StartupBean envBean;

    private ConcurrentHashMap envMap = null;

    /**
     * @Path("/v1/{uuid}")
     * @param mediaUUID
     * @param content
     * @param format
     *
     * @return binary or metadata.
     */
    @GET
    @HEAD
    @Path("/v1/{uuid: [\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png", "audio/ogg", "audio/wav", "audio/wav", "video/mp4", "video/ogg"})
    public Response getData(
            @PathParam("uuid") String mediaUUID,
            @QueryParam("content") String content,
            @QueryParam("format") String format) {
        logger.info("uuid " + mediaUUID);
        Response resp = Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();

        if (content != null && content.equals("metadata")) {
            logger.info("fetching metadata ");
            Media media = (Media) service.get(mediaUUID);
            return Response.status(Response.Status.OK).entity(media).build();
        }

        if (format != null) {
            logger.info("fetching mediafile with format " + format);
            resp = getBinaryMediafile(mediaUUID, format);
        }
        return resp;
    }

    /**
     * With additional 'metadata' before 'data' - transforms height. -
     * transforms to another image format
     * http://127.0.0.1:8080/MediaServerResteasy/media/v3/0a2b314e-9fb5-4084-a72f-937879dc221c?format=image/png&height=9
     *
     * @param mediaUUID
     * @param content
     * @param format
     * @param height
     * @return
     */
    @GET
    @HEAD
    @Path("/v2/{uuid: [\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png", "audio/ogg", "audio/wav", "audio/wav", "video/mp4", "video/ogg"})
    public Response _getDataVersion2(@PathParam("uuid") String mediaUUID,
            @QueryParam("content") String content,
            @QueryParam("format") String format,
            @QueryParam("height") Integer height) {
        final String API_VERSION = "2.0";
        logger.info("uuid " + mediaUUID);

        Response response = Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();

        if (content != null && content.equals("metadata")) {
            logger.info("fetching metadata ");
            Media media = (Media) service.get(mediaUUID);
            if (media != null) {
                Set<Lic> licenses = media.getLics();
                List<String> listLicenses = new ArrayList<>();
                for (Lic l : licenses) {
                    String lic = l.getAbbrev() + "-" + l.getVersion();
                    listLicenses.add(lic);
                }
                Set<MediaText> texts = media.getTexts();
                List<String> listDescription = new ArrayList<>();
                for (MediaText l : texts) {
                    String lang = l.getLang();
                    listDescription.add(lang);
                }

                String[] licenseArray = listLicenses.toArray(new String[listLicenses.size()]);
                String[] descArray = listDescription.toArray(new String[listDescription.size()]);
                MetadataHeader metadata = new MetadataHeader(API_VERSION, Response.Status.OK.getStatusCode(), licenseArray, descArray);

                Wrapper wrapper = new Wrapper(metadata, media);
                Response resp1 = Response.status(Response.Status.OK).entity(wrapper).build();
                return resp1;
            }
        }
        
        if (format != null && !format.isEmpty()) {
            logger.info("fetching mediafile with format " + format);
            response = _getBinaryMediafile(mediaUUID, format, height);
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).entity("The format missing").build();
        }

        return response;
    }

    @GET
    @Path("/base64/{uuid}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png"})
    public Response getEncodedMedia(@PathParam("uuid") String mediaUUID, @QueryParam("content") String content, @QueryParam("format") String format) {
        logger.info("fetching mediafile with format " + format);
        return getBase64Media(mediaUUID, format);
    }

    private Response getBase64Media(@PathParam("uuid") String uuid, @QueryParam("format") String format) {
        String filename = getDynamicPath(uuid, getBasePath());
        logger.info("with filename  " + filename);
        File file = new File(filename);
        boolean exists = file.exists();
        boolean canRead = file.canRead();
        logger.info("full filename exist [true || false ] == " + exists);
        logger.info("filename readable or not [true || false ] ==  " + canRead);
        Response response = returnBase64(file);
        return response;
    }

    private Response getBinaryMediafile(@PathParam("uuid") String uuid, @QueryParam("format") String format) {
        String filename = getDynamicPath(uuid, getBasePath());
        logger.info("with filename  " + filename);
        File file = new File(filename);
        boolean exists = file.exists();
        boolean canRead = file.canRead();
        logger.info("full filename exist [true || false ] == " + exists);
        logger.info("filename readable or not [true || false ] ==  " + canRead);
        Response response = returnFile(file);
        return response;
    }

    private Response _getBinaryMediafile(String uuid, String format, Integer height) {

        Response response = _returnFile(uuid, format, height);
        return response;
    }

    /**
     * http://localhost:8080/MediaServerResteasy/media/v1/search?view=flying&date=20140724
     * http://localhost:8080/MediaServerResteasy/media/f4bbe574-68eb-4423-b9e4-4384c6a3353c
     *
     *
     * @param uriInfo
     * @return
     */
    @GET
    @Path("/v1/search")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Media> showParameters(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> param = uriInfo.getQueryParameters();
        StringBuffer sb = buildKeyValueString(param);
        List<Media> mediaList = service.getMetadataByTags_MEDIA(sb.toString());

        return mediaList;
    }

    private StringBuffer buildKeyValueString(MultivaluedMap<String, String> param) {
        StringBuffer sb = new StringBuffer();
        final String DELIMITER = ":";
        final String SPLITTER = "&";

        if (param != null) {
            Iterator it = param.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = param.getFirst(key);
                sb.append(key).append(DELIMITER).append(value).append(SPLITTER);
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

    /**
     * @param uuid
     * @return
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll(@PathParam("uuid") String uuid) {

        boolean successfulDeletion = false;
        try {
            successfulDeletion = this.deleteMediaMetadata(uuid);
        } catch (Exception ex) {
            //logger.info("unsuccessful deletion of [".concat(uuid).concat("]"));g
            logger.debug(ex);
        }

        if (successfulDeletion) {
            successfulDeletion = this.deleteFileFromFS(uuid);
        }

        if (successfulDeletion) {
            return Response.status(204).entity("successful delete: " + uuid).build();
        }

        return Response.status(404).entity("unsuccessful delete: " + uuid).build();
    }

    private boolean deleteFileFromFS(@PathParam("mediaUUID") String mediaUUID) {
        boolean isFileDeleted = false;
        String fileName = this.getAbsolutePathToFile(mediaUUID);
        File file = new File(fileName);
        boolean exists = file.exists();
        if (exists) {
            isFileDeleted = file.delete();
        }
        return isFileDeleted;
    }

    private boolean deleteMediaMetadata(@PathParam("mediaUUID") String mediaUUID) {
        boolean deleted;
        deleted = service.deleteMediaMetadata(mediaUUID);
        return deleted;
    }

    /**
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.Decoder.html
     *
     * @param file
     * @return
     */
    private Response returnBase64(File file) {
        if (!file.exists()) {
            logger.info("File does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            String mimeType = getMimeType(file);
            InputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            byte[] encodeBase64 = Base64.encodeBase64(bytes);

            return Response.ok(encodeBase64, mimeType).build();
        } catch (IOException ioEx) {
            logger.info(ioEx);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Response returnFile(File file) {
        if (!file.exists()) {
            logger.info("File does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            String mimeType = getMimeType(file);
            InputStream inputStream = new FileInputStream(file);
            return Response.ok(inputStream, mimeType).build();
        } catch (IOException ioEx) {
            logger.info(ioEx);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Response _returnFile(String uuid, String formatMime, Integer height) {
        String filename = getDynamicPath(uuid, getBasePath());
        File file = new File(filename);

        if (!file.exists()) {
            logger.info("File does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
        BufferedImage transformedImage = null;

        try {
            String mimeType = getMimeType(file);
            String format = formatMime.substring(6);
            if (height != null) {
                transformedImage = this.getTransformed(uuid, height);
                ImageIO.write(transformedImage, format, outputStream);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                return Response.ok(inputStream, mimeType).build();
            } else {
                InputStream fis = new FileInputStream(file);
                transformedImage = ImageIO.read(fis);
                ImageIO.write(transformedImage, format, outputStream);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                return Response.ok(inputStream, mimeType).build();
            }
        } catch (IOException ioEx) {
            logger.info(ioEx);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private BufferedImage getTransformed(String uuid, int inHeight) throws IOException {

        String filename = getDynamicPath(uuid, getBasePath());

        File fileHandle = new File(filename);
        BufferedImage originalImage = ImageIO.read(fileHandle);
        int maxHeight = originalImage.getHeight();
        if (inHeight > maxHeight || inHeight <= 0) {
            inHeight = maxHeight;
        }

        BufferedImage image = Thumbnails.of(originalImage).height(inHeight).asBufferedImage();
        return image;
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        return mimeType;
    }

    private String getExtension(String mime) {
        int indexOf = mime.indexOf("/");
        String extension = mime.substring(indexOf + 1, mime.length());
        return extension;

    }

    private String getDynamicPath(String uuid, String path) {
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, path);
    }

    private String getBasePath() {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        return basePath;
    }

    private String getAbsolutePathToFile(String uuid) {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, basePath);
    }

    final int DEFAULT_LIMIT_SIZE_FOR_TYPES = 15;

    /**
     * Returning list in a 'Response' ( GenericEntity ) :
     * http://www.adam-bien.com/roller/abien/entry/jax_rs_returning_a_list
     *
     * @param type
     * @param minid
     * @param maxid
     * @return
     */
    @GET
    @Path("/v1/{type}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRangeOfImages(
            @PathParam("type") String type,
            @QueryParam("minid") Integer minid,
            @QueryParam("maxid") Integer maxid) {

        if (minid == null || maxid == null) {
            minid = 0;
            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
        }

        if (minid > maxid || (maxid - minid) > 1000) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Media> range = Collections.EMPTY_LIST;
        if (type.equals("images")) {
            range = service.findRange(Image.class, new int[]{minid, maxid});
        } else if (type.equals("sounds")) {
            range = service.findRange(Sound.class, new int[]{minid, maxid});
        } else if (type.equals("videos")) {
            range = service.findRange(Video.class, new int[]{minid, maxid});
        } else if (type.equals("attachments")) {
            range = service.findRange(Attachment.class, new int[]{minid, maxid});
        } else if (type.equals("all")) {
            range = service.findRange(Media.class, new int[]{minid, maxid});
        }

        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
        };

        Response build = Response.ok(list).build();
        return build;
    }

    @GET
    @Path("/v2/{type}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getVersion1RangeOfJSON(
            @PathParam("type") String type,
            @QueryParam("minid") Integer minid,
            @QueryParam("maxid") Integer maxid) {

        if (minid == null || maxid == null) {
            minid = 0;
            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
        }

        if (minid > maxid || (maxid - minid) > 1000) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Media> range = Collections.EMPTY_LIST;
        if (type.equals("images")) {
            range = service.findRange(Image.class, new int[]{minid, maxid});
        } else if (type.equals("sounds")) {
            range = service.findRange(Sound.class, new int[]{minid, maxid});
        } else if (type.equals("videos")) {
            range = service.findRange(Video.class, new int[]{minid, maxid});
        } else if (type.equals("attachments")) {
            range = service.findRange(Attachment.class, new int[]{minid, maxid});
        } else if (type.equals("all")) {
            range = service.findRange(Media.class, new int[]{minid, maxid});
        }

        GenericEntity<List<Media>> genericList = new GenericEntity<List<Media>>(range) {
        };

        List<Media> entities = genericList.getEntity();
        MetadataHeader metadata = new MetadataHeader("2.0", Response.Status.OK.getStatusCode());
        ListWrapper wrapper = new ListWrapper(metadata, entities);
        Response build = Response.ok(wrapper).build();
        return build;
    }

    @GET
    @Path("/v1/{type}/count")
    @Produces("text/plain")
    public Response countMedia(@PathParam("type") String type) {
        int count = 0;

        if (type.equals("images")) {
            count = service.count(Image.class);
        } else if (type.equals("sounds")) {
            count = service.count(Sound.class);
        } else if (type.equals("videos")) {
            count = service.count(Video.class);
        } else if (type.equals("attachments")) {
            count = service.count(Attachment.class);
        } else if (type.equals("all")) {
            count = service.count(Media.class);
        }

        return Response.ok(count).build();
    }

    @GET
    @Path("/v1/base64/{uuid}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png"})
    public Response getVersion1EncodedMedia(@PathParam("uuid") String mediaUUID, @QueryParam("content") String content, @QueryParam("format") String format) {
        return this.getEncodedMedia(mediaUUID, content, format);
    }

    @DELETE
    @Path("/v1/{uuid}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVersionAll(@PathParam("uuid") String uuid) {
        return this.deleteAll(uuid);
    }
    
    // <editor-fold defaultstate="collapsed" desc="using this before, one method for every 'type' as in 'media'/'image' and so forth.">
//     @GET
//    @Path("/v1/range/media")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getRangeOfMedia(
//            @QueryParam("minid") Integer minid,
//            @QueryParam("maxid") Integer maxid) {
//
//        if (minid == null || maxid == null) {
//            minid = 0;
//            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
//        }
//
//        if (minid > maxid || (maxid - minid) > 1000) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        List<Media> range = service.findRange(Media.class, new int[]{minid, maxid});
//        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
//        };
//
//        Response build = Response.ok(list).build();
//        return build;
//    }
//    
//    @GET
//    @Path("/v1/sounds")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getRangeOfSounds(@QueryParam("minid") Integer minid, @QueryParam("maxid") Integer maxid) {
//
//        if (minid == null || maxid == null) {
//            minid = 0;
//            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
//        }
//
//        if (minid > maxid || (maxid - minid) > 1000) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        List<Media> range = service.findRange(Sound.class, new int[]{minid, maxid});
//        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
//        };
//
//        Response build = Response.ok(list).build();
//        return build;
//    }
//
//    /**
//     *
//     * @param minid
//     * @param maxid
//     * @return
//     */
//    @GET
//    @Path("/v1/videos")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getRangeOfVideos(@QueryParam("minid") Integer minid, @QueryParam("maxid") Integer maxid) {
//
//        if (minid == null || maxid == null) {
//            minid = 0;
//            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
//        }
//
//        if (minid > maxid || (maxid - minid) > 1000) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        List<Media> range = service.findRange(Video.class, new int[]{minid, maxid});
//        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
//        };
//
//        Response build = Response.ok(list).build();
//        return build;
//    }
//
//    @GET
//    @Path("/v1/attachments")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getRangeOfAttachment(@QueryParam("minid") Integer minid, @QueryParam("maxid") Integer maxid) {
//
//        if (minid == null || maxid == null) {
//            minid = 0;
//            maxid = DEFAULT_LIMIT_SIZE_FOR_TYPES;
//        }
//
//        if (minid > maxid || (maxid - minid) > 1000) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        List<Media> range = service.findRange(Attachment.class, new int[]{minid, maxid});
//        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
//        };
//
//        Response build = Response.ok(list).build();
//        return build;
//    }
    //
//    @GET
//    @HEAD
//    @Path("/v2/{uuid: [\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}}")
//    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png", "audio/ogg", "audio/wav", "audio/wav", "video/mp4", "video/ogg"})
//    public Response getDataVersion2(@PathParam("uuid") String mediaUUID,
//            @QueryParam("content") String content,
//            @QueryParam("format") String format) {
//        final String API_VERSION = "2.0";
//        logger.info("uuid " + mediaUUID);
//
//        Response response = Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();
//
//        if (content != null && content.equals("metadata")) {
//            logger.info("fetching metadata ");
//            Media media = (Media) service.get(mediaUUID);
//            if (media != null) {
//                Set<Lic> licenses = media.getLics();
//                List<String> listLicenses = new ArrayList<>();
//                for (Lic l : licenses) {
//                    String lic = l.getAbbrev() + "-" + l.getVersion();
//                    listLicenses.add(lic);
//                }
//                Set<MediaText> texts = media.getTexts();
//                List<String> listDescription = new ArrayList<>();
//                for (MediaText l : texts) {
//                    String lang = l.getLang();
//                    listDescription.add(lang);
//                }
//
//                String[] licenseArray = listLicenses.toArray(new String[listLicenses.size()]);
//                String[] descArray = listDescription.toArray(new String[listDescription.size()]);
//                MetadataHeader metadata = new MetadataHeader(API_VERSION, Response.Status.OK.getStatusCode(), licenseArray, descArray);
//
//                Wrapper wrapper = new Wrapper(metadata, media);
//                Response resp1 = Response.status(Response.Status.OK).entity(wrapper).build();
//                return resp1;
//            }
//        }
//
//        if (format != null) {
//            logger.info("fetching mediafile with format " + format);
//            response = getBinaryMediafile(mediaUUID, format);
//        }
//
//        return response;
//    }
//    private BufferedImage getImage(String uuid) throws IOException {
//
//        String filename = getDynamicPath(uuid, getBasePath());
//
//        File fileHandle = new File(filename);
//        BufferedImage originalImage = ImageIO.read(fileHandle);
//
//        return originalImage;
//    }
    //    @GET
//    @Path("/v1/image/{uuid}")
//    @Produces({"image/jpeg", "image/png"})
//    public Response getImageByDimension(
//            @PathParam("uuid") String uuid,
//            @DefaultValue("image/png") @QueryParam("format") String formatMime,
//            @QueryParam("height") Integer height) {
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
//        BufferedImage transformedImage = null;
//
//        String format = formatMime.substring(6);
//
//        try {
//            if (uuid == null) {
//                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
//            }
//            if (height != null) {
//                transformedImage = this.getTransformed(uuid, height);
//            } else {
//                transformedImage = this.getImage(uuid);
//            }
//            ImageIO.write(transformedImage, format, outputStream);
//        } catch (IOException ex) {
//            logger.info(ex);
//        }
//
//        return Response.ok(outputStream.toByteArray()).build();
//    }
//    @GET
//    @Path("/v1/search/{tags}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Media> getMediaMetadataByLangAndTags(@PathParam("tags") String tags) {
//        String replaceAll = tags.replaceAll("=", ":");
//        List<Media> mediaList = service.getMetadataByTags_MEDIA(replaceAll);
//        
//        return mediaList;
//    }
    // </editor-fold>
}
