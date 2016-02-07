package se.nrm.bio.mediaserver.rs;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.json.simple.JSONObject;
import se.nrm.bio.mediaserver.business.MediaCouplingBean;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Attachment;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.Link;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.MediaText;
import se.nrm.bio.mediaserver.domain.Stream;
import se.nrm.bio.mediaserver.rs.coupling.DeterminationResourceFetch;
import se.nrm.mediaserver.resteasy.util.AggregateTags;
import se.nrm.mediaserver.resteasy.util.CheckSumFactory;
import se.nrm.mediaserver.resteasy.util.ExifExtraction;
import se.nrm.mediaserver.resteasy.util.FileSystemWriter;
import se.nrm.mediaserver.resteasy.util.FileUploadForm;
import se.nrm.mediaserver.resteasy.util.MediaFactory;
import se.nrm.mediaserver.resteasy.util.PathHelper;
import se.nrm.bio.mediaserver.util.TagHelper;
import se.nrm.mediaserver.resteasy.util.FileUploadJSON;
import se.nrm.mediaserver.resteasy.util.LinkUploadForm;
import se.nrm.mediaserver.resteasy.util.SingleLinkUploadForm;
import se.nrm.mediaserver.resteasy.util.UploadForm;
import se.nrm.mediaserver.resteasy.util.Writeable;

/**
 *
 * @author ingimar
 */
@Path("")
public class MediaResourceForm {

    private final static Logger logger = Logger.getLogger(MediaResourceForm.class);

    @EJB
    private MediaserviceBean bean;

    @EJB
    private MediaCouplingBean coupBean;

    @EJB
    private StartupBean envBean;

    ConcurrentHashMap envMap = null;

    private int dynamic_status = Response.Status.OK.getStatusCode();

    private final int STATUS_CONFLICT = Response.Status.CONFLICT.getStatusCode();

    private final int STATUS_INTERNAL_SERVER_ERROR = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

    public MediaResourceForm() {
    }

    @POST
    @Path("load")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createNewFile(@MultipartForm FileUploadForm form) throws IOException {
        envMap = envBean.getEnvironment();

        String mimeType = "unknown", hashChecksum = "unknown";
        final String NOT_APPLICABLE = "N/A";

        String displayOrder = form.getDisplayOrder();

        final byte[] fileData = form.getFileData();
        if (null == fileData || fileData.length == 0) {
            String msg = "attribute 'fileData' is null or empty \n";
            logger.info(msg);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR).entity(msg).build();
        }

        String fileUUID = generateRandomUUID();
        String uploadedFileLocation = getAbsolutePathToFile(fileUUID);
        if (uploadedFileLocation.isEmpty()) {
            String msg = "could not create a directory for the mediafile \n";
            logger.info(msg);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR).entity(msg).build();
        }
        logger.info("writing to file " + uploadedFileLocation);
        writeToFile(form, uploadedFileLocation);

        Tika tika = new Tika();
        mimeType = tika.detect(fileData);

        Media media = null;
        switch (mimeType) {
            case "image/tiff":
                logger.info("mediatype - image/tiff");
            case "image/png":
            case "image/jpeg":
            case "image/gif": {
                boolean exportImage = form.isExport();
                String exifJSON = NOT_APPLICABLE;
                String isExif = (String) envMap.get("is_exif");
                if (Boolean.parseBoolean(isExif)) {
                    try {
                        exifJSON = extractExif(uploadedFileLocation, exifJSON);
                    } catch (ImageProcessingException ex) {
                        logger.info(ex);
                    }
                }

                media = MediaFactory.createImage2(exportImage, exifJSON);

                break;
            }
            case "video/quicktime":
            case "video/mp4": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createVideo(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "audio/mpeg":
            case "audio/vorbis":
            case "audio/ogg": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createSound(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "application/pdf": {
                media = MediaFactory.createAttachement();
                break;
            }
        }

        if (null == media) {
            String msg = String.format("Mimetype [ %s ] is not supported \n", mimeType);
            logger.info("[media is null]: " + msg);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR).entity(msg).build();
        }

        hashChecksum = CheckSumFactory.createMD5ChecksumFromBytestream(fileData);

        String rootContext = (String) envMap.get("base_url");
        String pathToMedia = (String) envMap.get("relative_stream_url");

        media.setUuid(fileUUID);
        media.setOwner(form.getOwner());
        media.setFilename(form.getFileName());
        media.setMimetype(mimeType);
        media.setVisibility(form.getAccess());
        media.setHash(hashChecksum);
        String mediaURL = rootContext.concat(pathToMedia).concat(fileUUID);
        media.setMediaURL(mediaURL);

        AggregateTags aggr = new AggregateTags();

        String tagsConcatenated = aggr.aggregateTags(form);

        if (tagsConcatenated != null || !tagsConcatenated.isEmpty()) {
            addingTags(media, tagsConcatenated);
        }

        if (form.getLegend() == null || form.getLegend().isEmpty()) {
            form.setLegend(NOT_APPLICABLE);
        }

        if (form.getLegend() != null && !form.getLegend().isEmpty()) {
            MediaText mediaText;
            String comment = form.getComment();
            if (comment != null) {
                mediaText = new MediaText(form.getLegend(), form.getLanguage(), media, comment);
            } else {
                mediaText = new MediaText(form.getLegend(), form.getLanguage(), media);
            }
            media.addMediaText(mediaText);
        }

        final String licenceType = form.licenceType();
        if (licenceType != null) {
            Lic license = fetchFromDB(licenceType);
            media.getLics().add(license);
        }

        writeToDatabase(media);
        String responseOutput = fileUUID;

        Response build = Response.status(dynamic_status).header("mediaUUID", responseOutput).entity(responseOutput).build();

        return build;
    }

    /**
     * constraints : not able to extract from gif ?
     *
     * @param location
     * @param exifJSON
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    private String extractExif(String location, String exifJSON) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File(location));
        JSONObject jsonObject = packageEXIF_IN_JSON(metadata, true);
        exifJSON = jsonObject.toJSONString();
        final String prefix = "<![CDATA[";
        final String suffix = "]]>";
        exifJSON = prefix.concat(exifJSON).concat(suffix);
        return exifJSON;
    }

    /**
     * @TODO JUnit gives java.util.ConcurrentModificationException - must use
     * iterator and it.remove(); to handle this.
     *
     * @param updatedMediaText
     * @param media
     * @return
     */
    protected Media updateMediatext(final MediaText updatedMediaText, final Media media) {
        final String locale = updatedMediaText.getLang();

        // ie:temp
        final Set<MediaText> mediatextList = media.getTexts();

        String comment = "";
        try {
            Iterator<MediaText> it = mediatextList.iterator();
            while (it.hasNext()) {
                MediaText mediaText = it.next();
                comment = mediaText.getComment().trim();
                if (mediaText.getLang().equals(locale)) {
                    bean.delete(mediaText);
                    it.remove();
                }
            }
            if (!comment.isEmpty()) {
                if (comment.equals(updatedMediaText.getComment().trim())) {
                    updatedMediaText.setComment(comment);
                }
            }
            media.addMediaText(updatedMediaText);
        } catch (ConcurrentModificationException ex) {
            logger.info(ex);
            throw new ConcurrentModificationException("probz with MEDIA_TEXT", ex);
        }

        return media;
    }

    @PUT
    @Path("/upload-file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateFile(@MultipartForm FileUploadForm form) {
        String mediaUUID = form.getMediaUUID();
        Media media = (Media) bean.get(mediaUUID);

        String alt = form.getAlt(), access = form.getAccess(), fileName = form.getFileName();
        String legend = form.getLegend(), owner = form.getOwner(), tags = form.getTags();
        String comment = form.getComment();
        String displayOrder = form.getDisplayOrder();

        final String licenceType = form.licenceType();

        if (alt != null && !alt.equals("")) {
            media.setAlt(alt);
        }

        if (access != null && !access.equals("")) {
            media.setVisibility(access);
        }

        if (fileName != null && !fileName.equals("")) {
            media.setFilename(fileName);
        }

        if (legend != null && !legend.equals("")) {
            String language = form.getLanguage();
            MediaText mediatext = new MediaText(legend, language, media, comment);
            // kan förenklas . iner:2014-08-26
            updateMediatext(mediatext, media);
        }

        if (licenceType != null && !licenceType.equals("")) {
            updateLicense(licenceType, media);
        }

        if (owner != null && !owner.equals("")) {
            media.setOwner(owner);
        }

        if (tags != null && !tags.equals("")) {
            media.setTaggar(tags);
        }

        if (media instanceof Image) {
            Boolean export = form.isExport();
            if (export != null) {
                Image image = (Image) media;
                image.setIsExported(export);
                media = image;
            }

        } else if (media instanceof Stream) {
            String startTime = form.getStartTime(), endTime = form.getEndTime();
            Stream stream = (Stream) media;
            if (startTime != null && !startTime.equals("")) {
                stream.setStartTime(Integer.parseInt(startTime));
            }
            if (endTime != null && !endTime.equals("")) {
                stream.setStartTime(Integer.parseInt(endTime));
            }
            media = stream;

        } else if (media instanceof Attachment) {

        }

        writeToDatabase(media);

        int sortOrder = Integer.parseInt(displayOrder);
        String taxonUUID = media.getFirstDetermination().getTagValue();

        DeterminationResourceFetch up = new DeterminationResourceFetch();
        up.changeSortOrder(taxonUUID, mediaUUID, sortOrder);

        return null;
    }

    /**
     * ajax cannot call the @DELETE-directly.
     *
     * @param mediaUUID
     */
    @GET
    @Path("/ajax/delete/all/{mediaUUID}")
    public void ajaxDelete(@PathParam("mediaUUID") String mediaUUID) {
        boolean deleted = this.deleteAll(mediaUUID);
    }
   
    @DELETE
    @Path("/delete/all/{mediaUUID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean deleteAll(@PathParam("mediaUUID") String mediaUUID) {
        boolean successfulDeletion = false;
        try {
            successfulDeletion = this.deleteMediaMetadata(mediaUUID);
        } catch (Exception e) {
            logger.debug("unsuccessful deletion of [".concat(mediaUUID).concat("]"));
        }

        if (successfulDeletion) {
            successfulDeletion = this.deleteFileFromFS(mediaUUID);
        }
        
        return successfulDeletion;
    }

    @DELETE
    @Path("/delete/media/filesystem/{mediaUUID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean deleteFileFromFS(@PathParam("mediaUUID") String mediaUUID) {
        String fileName = this.getAbsolutePathToFile(mediaUUID);
        File file = new File(fileName);
        boolean isFileDeleted = file.delete();
        return isFileDeleted;
    }

    @DELETE
    @Path("/delete/media/metadata/{mediaUUID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean deleteMediaMetadata(@PathParam("mediaUUID") String mediaUUID) {
        boolean deleted;
        deleted = bean.deleteMediaMetadata(mediaUUID);
        return deleted;
    }

    /**
     * Sortorder-updated.
     *
     * @param form
     * @return
     */
    @POST
    @Path("/upload-coupling")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createNewCoupling(@MultipartForm SingleLinkUploadForm form) {
        Integer sortOrder = null;
        String taxonUUID = form.getTaxonUUID();
        String mediaUUID = form.getMediaUUID();
        if (null == form.getSortOrder()) {
            sortOrder = 999;
        } else {
            sortOrder = Integer.parseInt(form.getSortOrder());
        }

        Link link = Link.newInstanceWithSortOrder(form.getTypeOfSystem(), taxonUUID, form.getNameOfSystem(),
                form.getSystemURL(), mediaUUID, sortOrder);
        Response resp = createNewCoupling(link);

        DeterminationResourceFetch up = new DeterminationResourceFetch();
        up.changeSortOrder(taxonUUID, mediaUUID, sortOrder);

        int st = resp.getStatus();
        return Response.status(st).build();
    }

    @POST
    @Path("/upload-batch-coupling")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response coupling(@MultipartForm LinkUploadForm form) {
        return this.createNewCouplingParams(form);
    }

    /**
     * Skyttner used this when importing media from NF to here: curl -i -X POST
     * -H 'Content-Type: application/json' -d
     * '{"typeOfSystem":"NF_TAXON","taxonUUID":"taxon-nr","nameOfSystem":"NF_SYSTEM","systemURL":"NF_URL","mediaList":["e4a3cf7d-add4-4949-a6ce-0f5594e61970","ebb45da5-bd25-45af-8a04-3470d38523d1"]}'
     * http://172.16.23.62:8080/MediaServerResteasy/media/postJSONWithLIsta
     *
     * Should be an atomic transaction, either it succeeds or it fails. better
     * return-message : 200 would suffice
     *
     * sortOrder : Defaults settings with counter, starting from 1.
     *
     * @param form
     * @return
     */
    @POST
    @Path("/postJSONWithLIsta")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response createNewCouplingParams(@MultipartForm LinkUploadForm form) {
        String taxonUUID = form.getTaxonUUID();
        Link link = new Link(form.getTypeOfSystem(), taxonUUID, form.getNameOfSystem(), form.getSystemURL());

        String[] mediaList = form.getMediaList();

        int counter = 1;
        for (String mediaUUID : mediaList) {
            link.setSortOrder(counter);
            link.setMediaUUID(mediaUUID);
            Response resp = createNewCoupling(link);
            dynamic_status = resp.getStatus();
            counter++;
        }

        return Response.status(dynamic_status).build();
    }

    @POST
    @Path("/post")
    public Response createNewCoupling(Link link) {
        if (link == null) {
            return Response.status(STATUS_CONFLICT).build();
        }

        try {
            bean.save(link);
        } catch (Exception ex) {
            logger.info("" + ex);
            return Response.status(STATUS_CONFLICT).build();
        }

        return Response.status(dynamic_status).entity(link).build();
    }

    /**
     * 2014-10-23 : temporary fix. Created for migration of NRM-data, problem
     * with UTF-8
     *
     * @param form
     * @return
     */
    @POST
    @Path("/upload-file/base64")
    @Consumes(MediaType.APPLICATION_JSON)
//    @Interceptors(ContentTypeSetterPreProcessorInterceptor.class)
    public Response createNewMediaFile_JSON(FileUploadJSON form) {
        String mimeType = "unknown", hashChecksum = "unknown";

        String fileDataBase64 = form.getFileDataBase64();
        final byte[] fileData = DatatypeConverter.parseBase64Binary(fileDataBase64);
        if (null == fileData || fileData.length == 0) {
            String msg = "attribute 'fileData' is null or empty \n";
            logger.info(msg);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR).entity(msg).build();
        }

        String fileUUID = generateRandomUUID();
        String uploadedFileLocation = getAbsolutePathToFile(fileUUID);
        writeBase64ToFile(fileData, uploadedFileLocation);

        Tika tika = new Tika();
        mimeType = tika.detect(fileData);

        Media media = null;
        switch (mimeType) {
            case "image/tiff":
                logger.info("mediatype - image/tiff");
            case "image/png":
            case "image/jpeg":
            case "image/gif": {
//                boolean exportImage = form.getExport();
                boolean exportImage = false;
                String exifJSON = "N/A";
                String isExif = (String) envMap.get("is_exif");
                if (Boolean.parseBoolean(isExif)) {
                    try {
                        exifJSON = extractExif(uploadedFileLocation, exifJSON);
                    } catch (ImageProcessingException ex) {
                        logger.info(ex);
                    } catch (IOException ex) {
                        logger.info(ex);
                    }
                }
                media = MediaFactory.createImage2(exportImage, exifJSON);
                break;
//                boolean exportImage = form.getExport();
//                media = MediaFactory.createImage(exportImage);
//                break;
            }
            case "video/quicktime":
            case "video/mp4": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createVideo(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "audio/mpeg":
            case "audio/vorbis":
            case "audio/ogg": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createSound(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "application/pdf": {
                media = MediaFactory.createAttachement();
                break;
            }
        }

        if (null == media) {
            String msg = String.format("Mimetype [ %s ] is not supported \n", mimeType);
            logger.info("media is null:  ");
            return Response.status(STATUS_INTERNAL_SERVER_ERROR).entity(msg).build();
        }

        hashChecksum = CheckSumFactory.createMD5ChecksumFromBytestream(fileData);

        String pathToMedia = (String) envMap.get("relative_stream_url");

        media.setUuid(fileUUID);
        media.setOwner(form.getOwner());
        media.setFilename(form.getFileName());
        media.setMimetype(mimeType);
        media.setVisibility(form.getAccess());
        media.setHash(hashChecksum);
        media.setMediaURL(pathToMedia.concat("/").concat(fileUUID));

        if (form.getTags() != null) {
            String tags = form.getTags();
            if (!tags.equals("")) {
                addingTags(media, tags);
            }
        }

        if (form.getLegend() == null || form.getLegend().equals("")) {
            form.setLegend("N/A");
        }

        if (form.getLegend() != null && !form.getLegend().equals("")) {
            MediaText mediaText;
            String comment = form.getComment();
            if (comment != null) {
                mediaText = new MediaText(form.getLegend(), form.getLanguage(), media, comment);
            } else {
                mediaText = new MediaText(form.getLegend(), form.getLanguage(), media);
            }
            media.addMediaText(mediaText);
        }

        final String licenceType = form.getLicenseType();
        if (licenceType != null) {
            Lic license = fetchFromDB(licenceType);
            media.getLics().add(license);
        }

        writeToDatabase(media);
        String responseOutput = fileUUID;

        Response build = Response.status(dynamic_status).header("mediaUUID", responseOutput).entity(responseOutput).build();

        return build;
    }

    /**
     * Only one Licensetype for a media-file. That Constraint is in this method
     *
     * @param licenceType
     * @param media
     * @return
     */
    protected Media updateLicense(final String licenceType, Media media) {
        Lic updateLicense = fetchFromDB(licenceType);
        media.getLics().clear();
        media.getLics().add(updateLicense);
        return media;
    }

    private List<String> addingTags(Media media, String inTags) {
        TagHelper helper = new TagHelper();
        List<String> tags = helper.addingTags(media, inTags);
        return tags;
    }

    /**
     * @TODO Add some regexp later, change String to timedate
     *
     * @param time
     * @return
     */
    private int checkStartEndTime(String time) {
        if (null == time || time.equals("")) {
            time = "0";
        }

        return Integer.parseInt(time);
    }

    /**
     * https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html#randomUUID()
     * @return 
     */
    private String generateRandomUUID() {
        final String uuIdFilename = UUID.randomUUID().toString();
        return uuIdFilename;
    }

    private String getAbsolutePathToFile(String uuid) {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, basePath);
    }

    private void writeToFile(UploadForm form, String location) {
        Writeable writer = new FileSystemWriter();
        writer.writeBytesTo(form.getFileData(), location);
    }

    private void writeBase64ToFile(byte[] parseBase64Binary, String location) {
        Writeable writer = new FileSystemWriter();
        writer.writeBytesTo(parseBase64Binary, location);
    }

    private <T> void writeToDatabase(T media) {
        bean.save(media);
    }

    private Lic fetchFromDB(String abbrevation) {
        String trimmedAbbrevation = abbrevation.trim();
        Lic license = (Lic) bean.getNewLicenseByAbbr(trimmedAbbrevation); // 2015-07-15 with version

        return license;
    }

    /**
     * Removes 'tags' with key= 'Unknown'
     *
     * @param metadata
     * @param isFiltered
     * @return
     */
    private JSONObject packageEXIF_IN_JSON(Metadata metadata, boolean isFiltered) {
        ExifExtraction extract = new ExifExtraction();
        return extract.packageEXIF_IN_JSON(metadata, isFiltered);
    }
}
