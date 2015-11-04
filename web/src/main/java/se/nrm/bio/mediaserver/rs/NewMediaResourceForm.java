package se.nrm.bio.mediaserver.rs;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.json.simple.JSONObject;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Attachment;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.MediaText;
import se.nrm.bio.mediaserver.domain.Stream;
import se.nrm.bio.mediaserver.util.TagHelper;
import se.nrm.mediaserver.resteasy.util.CheckSumFactory;
import se.nrm.mediaserver.resteasy.util.ExifExtraction;
import se.nrm.mediaserver.resteasy.util.FileSystemWriter;
import se.nrm.mediaserver.resteasy.util.FileUploadJSON;
import se.nrm.mediaserver.resteasy.util.MediaFactory;
import se.nrm.mediaserver.resteasy.util.PathHelper;
import se.nrm.mediaserver.resteasy.util.Writeable;

/**
 *
 * @author ingimar
 */
@Path("")
public class NewMediaResourceForm {

    private final static Logger logger = Logger.getLogger(MediaResourceForm.class);

    @EJB
    private MediaserviceBean bean;

    @EJB
    private StartupBean envBean;

    private ConcurrentHashMap envMap = null;

    public NewMediaResourceForm() {
    }

    /**
     * curl -v -H "Accept: application/json" -H "Content-type: application/json"
     * -X POST -d @meta_and_image.json
     * http://127.0.0.1:8080/MediaServerResteasy/media
     *
     * @param form with base64-encoding.
     * @return
     */
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMedia(FileUploadJSON form) {
        String mimeType = "unknown", hashChecksum = "unknown";
        String msg = "attribute 'fileData' is null or empty \n";
        // Check, howto avoid long-stacktrace if file is forgotten ?
        if (form == null) {
            logger.debug(msg);
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).build();
        }
        String fileDataBase64 = form.getFileDataBase64();
        if (null == fileDataBase64 || fileDataBase64.isEmpty()) {
            logger.debug(msg);
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).build();
        }
        byte[] fileData = DatatypeConverter.parseBase64Binary(fileDataBase64);

        if (null == fileData || fileData.length == 0) {
            logger.debug(msg);
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).build();
        }

        String fileUUID = generateRandomUUID();
        String uploadedFileLocation = getAbsolutePathToFile(fileUUID);
        writeBase64ToFile(fileData, uploadedFileLocation);

        Tika tika = new Tika();
        mimeType = tika.detect(fileData);

        Media media = null;
        String mediaType = mimeType.substring(0, mimeType.indexOf("/"));

        switch (mediaType) {
            case "image": {
                boolean exportImage = form.getExport();
                String exifJSON = "N/A";
                String isExif = (String) envMap.get("is_exif");
                if (Boolean.parseBoolean(isExif)) {
                    try {
                        exifJSON = extractExif(uploadedFileLocation, exifJSON);
                    } catch (ImageProcessingException | IOException ex) {
                        logger.info(ex);
                    }
                }
                media = MediaFactory.createImage2(exportImage, exifJSON);
                break;
            }
            case "video": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createVideo(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "audio": {
                String startTime = form.getStartTime(), endTime = form.getEndTime();
                media = MediaFactory.createSound(checkStartEndTime(startTime), checkStartEndTime(endTime));
                break;
            }
            case "text":
            case "application": {
                media = MediaFactory.createAttachement();
                break;
            }
        }

        if (null == media) {
            logger.info("media is null:  ");
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).build();
        }

        hashChecksum = CheckSumFactory.createMD5ChecksumFromBytestream(fileData);

        media.setUuid(fileUUID);
        media.setOwner(form.getOwner());
        media.setFilename(form.getFileName());
        media.setMimetype(mimeType);
        media.setVisibility(form.getAccess());
        media.setHash(hashChecksum);
        final String mediaURL = createMediaURL(fileUUID, mimeType);
        media.setMediaURL(mediaURL);

        if (form.getTaggar() != null) {
            String[] taggar = form.getTaggar();
            String splitter = TagHelper.getSplitter();
            String joinTags = StringUtils.join(taggar, splitter);
            addingTags(media, joinTags);
        }

        final String legend = form.getLegend();
        if (legend == null || legend.isEmpty()) {
            form.setLegend("N/A");
        }

        if (legend != null && !legend.isEmpty()) {
            MediaText mediaText;
            String comment = form.getComment();
            if (comment != null) {
                mediaText = new MediaText(legend, form.getLanguage(), media, comment);
            } else {
                mediaText = new MediaText(legend, form.getLanguage(), media);
            }
            media.addMediaText(mediaText);
        }

        final String licenceType = form.getLicenseType();
        if (licenceType != null) {
            Lic license = fetchNewLicenseFromDB(licenceType); // enhanced with version, check the liquibase ( foreign key constraints)
            media.getLics().add(license);
        }

        writeToDatabase(media);
        Response response = Response.status(Response.Status.CREATED).entity(media).build();

        return response;
    }

    private String createMediaURL(String fileUUID, String mimeType) {
        String host = (String) envMap.get("mediaserver_host");
        String baseURL = (String) envMap.get("base_url");
        String pathToMedia = (String) envMap.get("relative_new_stream_url");
        final String mediaURL = host.concat("/").concat(baseURL).concat("/").concat(pathToMedia).concat("/").concat(fileUUID).concat("?format=").concat(mimeType);
        return mediaURL;
    }

    /**
     * @param form with base64-encoding.
     * @return 
     */
    @PUT
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMedia(FileUploadJSON form) {

        String mediaUUID = form.getMediaUUID();
        if (null == mediaUUID) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Media media = (Media) bean.get(mediaUUID);
        if (null == media) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String base64EncodedFile = form.getFileDataBase64();
        if (base64EncodedFile != null) {
            final byte[] fileData = DatatypeConverter.parseBase64Binary(base64EncodedFile);
            String uploadedFileLocation = getAbsolutePathToFile(mediaUUID);
            writeBase64ToFile(fileData, uploadedFileLocation);

            Tika tika = new Tika();
            String mimeType = tika.detect(fileData);
            media.setMimetype(mimeType);
            final String mediaURL = createMediaURL(mediaUUID, mimeType);
            media.setMediaURL(mediaURL);
        }

        // fetch from form.
        String alt = form.getAlt(), access = form.getAccess(), fileName = form.getFileName();
        String legend = form.getLegend(), owner = form.getOwner(); //  tags = form.getTags();

        String[] tags = form.getTaggar();

        String comment = form.getComment();
        String licenceType = form.getLicenseType();

        if (alt != null && !alt.isEmpty()) {
            media.setAlt(alt);
        }

        if (access != null && !access.isEmpty()) {
            media.setVisibility(access);
        }

        if (fileName != null && !fileName.isEmpty()) {
            media.setFilename(fileName);
        }

        if (legend != null && !legend.isEmpty()) {
            String language = form.getLanguage();
            MediaText mediatext = new MediaText(legend, language, media, comment);
            // kan förenklas . iner:2014-08-26
            updateMediatext(mediatext, media);
        }

        if (licenceType != null && !licenceType.isEmpty()) {
            updateLicense(licenceType, media);
        }

        if (owner != null && !owner.isEmpty()) {
            media.setOwner(owner);
        }

//        if (tags != null && !tags.isEmpty()) {
//            media.setTaggar(tags);
//        }
        if (media instanceof Image) {
            Boolean export = form.getExport();
            if (export != null) {
                Image image = (Image) media;
                image.setIsExported(export);
                media = image;
            }

        } else if (media instanceof Stream) {
            String startTime = form.getStartTime(), endTime = form.getEndTime();
            Stream stream = (Stream) media;
            if (startTime != null && !startTime.isEmpty()) {
                stream.setStartTime(Integer.parseInt(startTime));
            }
            if (endTime != null && !endTime.isEmpty()) {
                stream.setStartTime(Integer.parseInt(endTime));
            }
            media = stream;

        } else if (media instanceof Attachment) {

        }

        writeToDatabase(media);
        Response response = Response.status(Response.Status.OK).entity(media).build();

        return response;
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
     * Only one Licensetype for a media-file. That Constraint is in this method
     *
     * @param licenceType
     * @param media
     * @return
     */
    protected Media updateLicense(final String licenceType, Media media) {
        Lic updateLicense = fetchNewLicenseFromDB(licenceType);
        media.getLics().clear();
        media.getLics().add(updateLicense);
        return media;
    }

    private void addingTags(Media media, String inTags) {
        TagHelper helper = new TagHelper();
        helper.addingTags(media, inTags);
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

    private String generateRandomUUID() {
        final String uuIdFilename = UUID.randomUUID().toString();
        return uuIdFilename;
    }

    private String getAbsolutePathToFile(String uuid) {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        logger.debug("Reading path from database : " + basePath);
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, basePath);
    }

    private void writeBase64ToFile(byte[] parseBase64Binary, String location) {
        Writeable writer = new FileSystemWriter();
        writer.writeBytesTo(parseBase64Binary, location);
    }

    private <T> void writeToDatabase(T media) {
        bean.save(media);
    }

    private Lic fetchNewLicenseFromDB(String abbrAndLicense) {
        String trimmedAbbrevation = abbrAndLicense.trim();

        int indexOfVersion = trimmedAbbrevation.indexOf('v');
        if (indexOfVersion == -1) {
            String defaultLicense = (String) envMap.get("default_CC_license");
            trimmedAbbrevation = abbrAndLicense.concat(" ").concat(defaultLicense);
        }

        Lic license = (Lic) bean.getNewLicenseByAbbr(trimmedAbbrevation);
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

    protected Media updateMediatext(final MediaText updatedMediaText, final Media media) {
        final String locale = updatedMediaText.getLang();

        // ie:temp
        final Set<MediaText> currentMediatextList = media.getTexts();

        String comment = "";
        try {
            Iterator<MediaText> it = currentMediatextList.iterator();
            while (it.hasNext()) {
                MediaText mediaText = it.next();
//                comment = mediaText.getComment().trim();
                if (mediaText.getLang().equals(locale)) {
                    bean.delete(mediaText);
                    it.remove();
                }
            }
//            if (!comment.isEmpty()) {
//                if (comment.equals(updatedMediaText.getComment().trim())) {
//                    updatedMediaText.setComment(comment);
//                }
//            }
            media.addMediaText(updatedMediaText);
        } catch (ConcurrentModificationException ex) {
            logger.info(ex);
            throw new ConcurrentModificationException("probz with MEDIA_TEXT", ex);
        }

        return media;
    }

}
