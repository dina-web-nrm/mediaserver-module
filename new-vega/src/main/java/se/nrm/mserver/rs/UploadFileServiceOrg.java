package se.nrm.mserver.rs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ejb.EJB;

import org.apache.log4j.Logger;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.business.StartupBean;

/**
 * http://localhost:8080/RESTfulExample/rest/file/upload
 *
 * @author ingimar
 */
//@Path("/file")
public class UploadFileServiceOrg {

//    private final static Logger logger = Logger.getLogger(UploadFileServiceOrg.class);
//
//    @EJB
//    private MediaserviceBean bean;
//    
//    @EJB
//    private StartupBean envBean;
//
//    private ConcurrentHashMap envMap = null;
//    
//    public UploadFileServiceOrg() {
//    }
//
//    @POST
//    @Path("/upload")
//    @Consumes("multipart/form-data")
//    public Response uploadFile(@MultipartForm FileUploadForm form) {
//        
//        logger.info("bean injected "+bean);
//        logger.info("form is " + form);
//        logger.info("Filename is " + form.getFilename());
//        logger.info("Licensetyp is " + form.getLicenseType());
//        logger.info("owner is " + form.getOwner());
//
//        String fileName;
//        fileName = "/tmp/uploader/xxxx.image";
//
//        try {
//            writeFile(form.getData(), fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Done");
//
//        return Response.status(200)
//                .entity("uploadFile is called, Uploaded file name : " + fileName).build();
//
//    }
//
//    // save to somewhere
//    private void writeFile(byte[] content, String filename) throws IOException {
//
//        File file = new File(filename);
//
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//
//        FileOutputStream fop = new FileOutputStream(file);
//
//        fop.write(content);
//        fop.flush();
//        fop.close();
//
//    }
//
//    private boolean saveToFile(InputStream inStream, String target) throws IOException {
//        boolean isSuccess = false;
//        if (inStream != null) {
//            logger.info("InputStream available?  " + inStream.available());
//        } else {
//            logger.info("InputStream: null  ");
//            return isSuccess;
//        }
//        logger.info("target is " + target);
//        OutputStream out = null;
//        int read = 0;
//        byte[] bytes = new byte[1024];
//
//        out = new FileOutputStream(new File(target));
//        while ((read = inStream.read(bytes)) != -1) {
//            out.write(bytes, 0, read);
//        }
//        isSuccess = true;
//
//        out.flush();
//        out.close();
//        return isSuccess;
//    }
    

}
