package se.nrm.mserver.rs;

import se.nrm.mserver.util.FileSystemWriter;
import se.nrm.mserver.util.MediaFactory;
import se.nrm.mserver.util.PathHelper;
import se.nrm.mserver.util.Writeable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ejb.EJB;
import javax.ws.rs.GET;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Media;

/**
 * /subsystem=undertow/server=default-server/http-listener=default/:write-attribute(name=max-post-size,value=1048576000)
 * http://localhost:8080/RESTfulExample/rest/file/upload
 *
 * @author ingimar
 */
@Path("/file")
public class UploadFileService {

    private final static Logger logger = Logger.getLogger(UploadFileService.class);

    @EJB
    private MediaserviceBean bean;

    @EJB
    private StartupBean envBean;

    private ConcurrentHashMap envMap = null;

    public UploadFileService() {
    }
    
    @GET
    @Path("/check")
    public Response isOK(){
        String message = "server is up";
         logger.info("@GET bean-1 "+bean);
         logger.info("@GET-bean-2 "+envBean);
        if (bean != null && envBean != null){
            message.concat(" - and reaching ejb-beans");
        }
        logger.info("@GET-endpoint with msq = ".concat(message));
        return Response.status(200).entity(message).build();
    }

    @POST
    @Path("/vega")
    @Consumes("multipart/form-data")
    public Response uploadFile(@MultipartForm FileUploadForm form) {
        String owner = form.getOwner();
        String filename = form.getFilename();
        byte[] data = form.getData();

        logger.info("Filename is " + filename);
        logger.info("owner is " + owner);
        String uuid = generateRandomUUID();
        logger.info("UUID is " + uuid);
        String mimeType="N/A";
        Tika tika = new Tika();
        mimeType = tika.detect(data);
        Media media = MediaFactory.createAttachement(owner, "public", filename, mimeType);
        media.setUuid(uuid);
        String mediaURL = createMediaURL(uuid, mimeType);
        media.setMediaURL(mediaURL);
        
        writeToFS(data, uuid);
        writeToDB(media);

        return Response.status(200)
                .entity("uploadFile is called, Uploaded file : ").build();
    }

    private <T> void writeToDB(T media) {
        bean.save(media);
    }

    private void writeToFS(byte[] bytes, String fileUUID) {
        String location = getAbsolutePathToFile(fileUUID);
        logger.info("uploadedFileLocation " + location);
        Writeable writer = new FileSystemWriter();
        writer.writeBytesTo(bytes, location);
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

    private String createMediaURL(String fileUUID, String mimeType) {
        envMap = envBean.getEnvironment();
        String host = (String) envMap.get("mediaserver_host");
        String baseURL = (String) envMap.get("base_url");
        String pathToMedia = (String) envMap.get("relative_new_stream_url");
        final String mediaURL = host.concat("/").concat(baseURL).concat("/").concat(pathToMedia).concat("/").concat(fileUUID).concat("?format=").concat(mimeType);
        return mediaURL;
    }
}
