package se.nrm.mserver.rs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.mserver.util.PathHelper;

/**
 * same as in the 'web'-module (2019-12-09)
 * 
 * @author ingimar
 */
@Path("")
public class FetchMediaResource {
    
     private final static Logger logger = Logger.getLogger(FetchMediaResource.class);

    @EJB
    private MediaserviceBean service;

    @EJB
    protected StartupBean envBean;

    private ConcurrentHashMap envMap = null;
    
    @GET
    @HEAD
    @Path("/v1/{uuid: [\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}}")
    @Produces({MediaType.APPLICATION_JSON, "image/jpeg", "image/png", "audio/ogg", "audio/wav", "audio/wav", "video/mp4", "video/ogg"})
    public Response getData(
            @PathParam("uuid") String mediaUUID,
            @QueryParam("content") String content,
            @QueryParam("format") String format) {
        logger.info("uuid " + mediaUUID);
        Response response = Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();

        if (content != null && content.equals("metadata")) {
            logger.info("fetching metadata ");
            Media media = (Media) service.get(mediaUUID);
            return Response.status(Response.Status.OK).entity(media).build();
        }

        if (format != null) {
            logger.info("fetching mediafile with format " + format);
            response = getV1BinaryMediafile(mediaUUID, format);
        }
        
        return response;
    }
    
    private Response getV1BinaryMediafile(@PathParam("uuid") String uuid, @QueryParam("format") String format) {
        Response response = Response.status(Response.Status.FORBIDDEN).entity("File does not exist or the file is not readable").build();

        String filename = getDynamicPath(uuid, getBasePath());
        File file = new File(filename);

        boolean checkFilestatus = this.checkFilestatus(file);
        if (checkFilestatus) {
            response = returnFile(file);
        }

        return response;
    }
    
    private String getDynamicPath(String uuid, String path) {
        return PathHelper.getEmptyOrAbsolutePathToFile(uuid, path);
    }
    
     private String getBasePath() {
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("path_to_files");
        return basePath;
    }

      private boolean checkFilestatus(File file) {
        boolean isSolid = (file.exists() && file.canRead());
        logger.info("does the file exist and is it readable ? " + isSolid);
        return isSolid;
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
       
        private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        return mimeType;
    }
}
