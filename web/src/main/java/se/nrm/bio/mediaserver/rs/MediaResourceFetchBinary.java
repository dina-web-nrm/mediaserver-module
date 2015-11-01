package se.nrm.bio.mediaserver.rs;

import java.io.FileInputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;

import javax.imageio.ImageIO;
import javax.ws.rs.Produces;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import se.nrm.bio.mediaserver.business.StartupBean;
import se.nrm.mediaserver.resteasy.util.PathHelper;

/**
 *
 * @author ingimar
 */
@Path("/")
@Produces({"image/jpeg", "image/png"})
@Deprecated
public class MediaResourceFetchBinary {

    private final static Logger logger = Logger.getLogger(MediaResourceFetchBinary.class);

    @EJB
    private StartupBean envBean;

    private ConcurrentHashMap envMap = null;
    
    /**
     *  temp :  an add on for the naturalist 
     * @param uuid
     * @return 
     */
    @GET
    @Path("/stream/{uuid}")
    @Deprecated
    public Response getMedia(@PathParam("uuid") String uuid) {
        String naturforskaren = uuid;
        if (uuid.contains(".")) {
            int indexOf = uuid.indexOf('.');
            naturforskaren = uuid.substring(0, indexOf);
        }
        String filename = getDynamicPath(naturforskaren, getBasePath());

        File file = new File(filename);
        Response response = returnFile(file);
        return response;
    }

    @GET
    @Path("/stream/image/{uuid}/{height}")
    @Produces({"image/jpeg", "image/png"})
    @Deprecated
    public byte[] getImageByDimension(@PathParam("uuid") String uuid, @PathParam("height") String height) {
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

    /**
     *
     * @param info
     * @param uuid
     * @return
     * @throws IOException
     */
    private BufferedImage getTransformed(String uuid, String inHeight) throws IOException {
        int height = 150;
        if (inHeight != null) {
            height = Integer.parseInt(inHeight);
            if (height < 100) {
                height = 150;
            }
        }

        String filename = getDynamicPath(uuid, getBasePath());

        File fileHandle = new File(filename);
        BufferedImage originalImage = ImageIO.read(fileHandle);
        int maxHeight = originalImage.getHeight();
        if (height > maxHeight || height <= 0) {
            height = maxHeight;
        }
        BufferedImage image = Thumbnails.of(originalImage).height(height).asBufferedImage();

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
