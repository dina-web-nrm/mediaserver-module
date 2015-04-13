package se.nrm.bio.mediaserver.rs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Media;
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
    private StartupBean envBean;

    private ConcurrentHashMap envMap = null;

    /**
     * curl http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?content=metadata
     * http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?format=image/jpeg
     * 
     * @param mediaUUID
     * @param content
     * @param format
     * 
     * @return binary or metadata.
     */
    @GET
    @Path("/{uuid}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png"})
    public Response getMetadata(@PathParam("uuid") String mediaUUID, @QueryParam("content") String content, @QueryParam("format") String format) {
        logger.info("uuid " + mediaUUID);
        if (content != null && content.equals("metadata")) {
            logger.info("fetching metadata ");
            Media media = (Media) service.get(mediaUUID);
            return Response.status(200).entity(media).build();
        }

     //   if (format != null && (format.equals("image/jpeg") || format.equals("image/png"))) {
        if (format != null){
            logger.info("fetching mediafile with format " + format);
            return getMedia(mediaUUID, format);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();
    }

    private Response getMedia(@PathParam("uuid") String uuid, @QueryParam("format") String format) {
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

   /**
    * http://localhost:8080/MediaServerResteasy/media/image/863ec044-17cf-4c87-81cc-783ab13230ae?format=image/jpeg&height=150
    * 
    * @param uuid
    * @param format, format=image/jpeg
    * @param height
    * @return 
    */
    @GET
    @Path("/image/{uuid}")
    @Produces({"image/jpeg", "image/png"})
    public byte[] getImageByDimension(@PathParam("uuid") String uuid, @QueryParam("format") String format, @QueryParam("height") int height) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);

        try {
            BufferedImage transformedImage = this.getTransformed(uuid, height);
            ImageIO.write(transformedImage, "jpeg", outputStream);
        } catch (IOException ex) {
            logger.info(ex);
        }

        return outputStream.toByteArray();
    }

    
//    @GET
//    @Path("/v1/search/{tags}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Media> getMediaMetadataByLangAndTags(@PathParam("tags") String tags) {
//        String replaceAll = tags.replaceAll("=", ":");
//        List<Media> mediaList = service.getMetadataByTags_MEDIA(replaceAll);
//        
//        return mediaList;
//    }
    
    /**
     * http://localhost:8080/MediaServerResteasy/media/v1/search?view=flying&date=20140724
     * http://localhost:8080/MediaServerResteasy/media/f4bbe574-68eb-4423-b9e4-4384c6a3353c

     * 
     * @param uriInfo
     * @return
     */
    @GET
    @Path("/v1/search")
    @Produces({MediaType.APPLICATION_JSON})
    public  List<Media> showParameters(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> param = uriInfo.getQueryParameters();

        StringBuffer sb = buildKeyValueString(param);
         List<Media> mediaList = service.getMetadataByTags_MEDIA(sb.toString());
         
        return mediaList;
    }

    private StringBuffer buildKeyValueString(MultivaluedMap<String, String> param) {
        StringBuffer sb = new StringBuffer();
        String delimiter =":";
        String splitter ="&";
        if (param != null) {
            Iterator it = param.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = param.getFirst(key);
                sb.append(key).append(delimiter).append(value).append(splitter);
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb;
    }

    /**
     * curl -i -H "Accept: application/json" -X DELETE   http://localhost:8080/MediaServerResteasy/media/f4bbe574-68eb-4423-b9e4-4384c6a3353c
     * @param uuid
     * @return 
     */
    @DELETE
    @Path("/image/{uuid}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll(@PathParam("uuid") String uuid) {
        boolean successfulDeletion = false;
        try {
            successfulDeletion = this.deleteMediaMetadata(uuid);
        } catch (Exception ex) {
            logger.info("unsuccessful deletion of [".concat(uuid).concat("]"));
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

    private static Response returnFile(File file) {
        if (!file.exists()) {
            logger.info("File does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            String mimeType = getMimeType(file);
            FileInputStream fileInputStream = new FileInputStream(file);
            return Response.ok(fileInputStream, mimeType).build();
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

    private static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        return mimeType;
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
}
