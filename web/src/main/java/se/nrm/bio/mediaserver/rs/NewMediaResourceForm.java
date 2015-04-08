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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
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

    private int dynamic_HTTP_STATUS = 404;

    public NewMediaResourceForm() {
    }

    /**
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @meta_and_image.json http://127.0.0.1:8080/MediaServerResteasy/media
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

        String fileDataBase64 = form.getFileDataBase64();
        final byte[] fileData = DatatypeConverter.parseBase64Binary(fileDataBase64);
        if (null == fileData || fileData.length == 0) {
            String msg = "attribute 'fileData' is null or empty \n";
            logger.info(msg);
            return Response.status(500).entity(msg).build();
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
            return Response.status(500).entity(msg).build();
        }

        hashChecksum = CheckSumFactory.createMD5ChecksumFromBytestream(fileData);

        

        media.setUuid(fileUUID);
        media.setOwner(form.getOwner());
        media.setFilename(form.getFileName());
        media.setMimetype(mimeType);
        media.setVisibility(form.getAccess());
        media.setHash(hashChecksum);
        final String mediaURL = createMediaURL(fileUUID,mimeType);
        media.setMediaURL(mediaURL);

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
//        String responseOutput = fileUUID;

        Response response = Response.status(201).entity(media).build();

        return response;
    }

    private String createMediaURL(String fileUUID, String mimeType) {
        String host = (String) envMap.get("mediaserver_host");
        String baseURL = (String) envMap.get("base_url");
        String pathToMedia = (String) envMap.get("relative_new_stream_url");
        final String mediaURL = host.concat("/").concat(baseURL).concat("/").concat(pathToMedia).concat("/").concat(fileUUID).concat("?format=").concat(mimeType);
        return mediaURL;
    }

    @PUT
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMedia(FileUploadJSON form) {
        final int HTTP_STATUS_NOT_FOUND = 404;
        
        String mediaUUID = form.getMediaUUID(); 
        if (null == mediaUUID) {
            return Response.status(HTTP_STATUS_NOT_FOUND).build();
        }
        
        Media media = (Media) bean.get(mediaUUID);
        if (null == media) {
            return Response.status(HTTP_STATUS_NOT_FOUND).build();
        }

        String base64EncodedFile = form.getFileDataBase64();
        final byte[] fileData = DatatypeConverter.parseBase64Binary(base64EncodedFile);
        String uploadedFileLocation = getAbsolutePathToFile(mediaUUID);
        writeBase64ToFile(fileData, uploadedFileLocation);

        Tika tika = new Tika();
        String mimeType = tika.detect(fileData);
        media.setMimetype(mimeType);
        
        // fetch from form.
        String alt = form.getAlt(), access = form.getAccess(), fileName = form.getFileName();
        String legend = form.getLegend(), owner = form.getOwner(), tags = form.getTags();
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

        if (tags != null && !tags.isEmpty()) {
            media.setTaggar(tags);
        }

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
        Response response = Response.status(200).entity(media).build();

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

    private String generateRandomUUID() {
        final String uuIdFilename = UUID.randomUUID().toString();
        return uuIdFilename;
    }

    private String getAbsolutePathToFile(String uuid) {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, basePath);
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
        Lic license = (Lic) bean.getLicenseByAbbr(trimmedAbbrevation);

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