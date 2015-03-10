package se.nrm.bio.mediaserver.rs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.mediaserver.resteasy.util.PathHelper;

/**
 *
 * @author ingimar
 */
@Path("/")

public class NewMediaResource {

    private final static Logger logger = Logger.getLogger(MediaResource.class);

    @EJB
    private MediaserviceBean service;

    @EJB
    private StartupBean envBean;

    private ConcurrentHashMap envMap = null;

    @GET
    @Path("/{uuid}")
//    @Produces({MediaType.APPLICATION_JSON,"image/*"})
    public Response getMetadata(@PathParam("uuid") String mediaUUID, @QueryParam("content") String content, @QueryParam("format") String format) {
        if (content != null && content.equals("metadata")) {

            Media media = (Media) service.get(mediaUUID);
            return Response.status(200).entity(media).build();
        }
        // treated same as for now
        if (format != null && (format.equals("image/jpeg") || format.equals("image/png")) ) {

            return getMedia(mediaUUID, format);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + mediaUUID).build();
    }

    public Response getMedia(@PathParam("uuid") String uuid, @QueryParam("format") String format) {
        String filename = getDynamicPath(uuid, getBasePath());

        File file = new File(filename);
        Response response = returnFile(file);
        return response;
    }

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
}
