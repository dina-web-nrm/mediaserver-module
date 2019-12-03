package se.nrm.bio.mediaserver.clients;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * following Idas file ;
 * <p>
 * @see dina-vegacenter-logic, se.nrm.dina.vegacenter.Main.java
 *
 * @author ingimar
 */
public class ClientEncodeAndPostfile {

    public static void main(String[] args) throws IOException, JSONException {
        System.out.println("Starting ".concat("ClientPostEncodedFiles"));
        String URL = Util.getLocalURL();
        
        // proof-of-concept, works for this file (129kb)
        String filePath = "/media/real-vegadare/vegadare-share/zipfiles/Testing.zip";

        // proof-of-concept, works for this file (4.4kb)
        //String filePath = "/media/real-vegadare/vegadare-share/zipfiles/marley.jpeg";

        System.out.println("\t Post to the Mediaserver : ".concat(URL).concat("\n"));

        FileInputStream inputStream = new FileInputStream(filePath);

        //stream is sent to
        final byte[] bytes64bytes = Base64.encodeBase64(IOUtils.toByteArray(inputStream));
        final String contentToBeSaved = new String(bytes64bytes);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;

        JSONObject metadata = new JSONObject();
        metadata.put("owner", "ida li");
        metadata.put("access", "public");
        metadata.put("licenseType", "CC BY");
        metadata.put("fileName", "fileName");
        metadata.put("fileDataBase64", contentToBeSaved);
        
        String metadataFormatted = StringEscapeUtils.unescapeJavaScript(metadata.toString());
        boolean validateJSON = validateJSON(metadataFormatted);

        StringEntity entity = new StringEntity(metadataFormatted, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(URL);
        post.setEntity(entity);
        response = client.execute(post);
        HttpEntity responseEntity = response.getEntity();

        String responseFromMediaserver = EntityUtils.toString(responseEntity, "UTF-8");
        System.out.println("\n Response from the mediaserver is : " + responseFromMediaserver);

        JSONObject json = new JSONObject(responseFromMediaserver);
        String uuid = json.getString("uuid");
        System.out.println("UUID is " + uuid);
    }

    private static boolean validateJSON(String jsonInString) {
        boolean isValid = false;

        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }

    }
}

//
//    private void starter() {
//        final String FINAL_SAVED_FILENAME = "Testing";
//        final String MACHINE_LOCAL = this.getMachine();
//        final String WORKING_LOCAL_PATH = this.getWorkingLocalPath();
//
//        String zipFilePath = zipDirectory(FINAL_SAVED_FILENAME, MACHINE_LOCAL, WORKING_LOCAL_PATH);
//        String uuid = null;
//    }
//
//    public String postFileToMediaServer(String directory, User user) {
//
//        String uuid = null;
//        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
//        try {
//            client = clientBuilder.build();
//            target = client.target(MEDIA_SERVER_URL);
//
//            String requestString = buildRequestString(directory, user);
//            Response response = target.request().post(Entity.entity(requestString, "application/json"));
//            logger.info("response : {}", response.getStatus());
//
//            String jsonString = response.readEntity(String.class);
//            logger.info("response string : {}", jsonString);
//
//            JSONObject json = new JSONObject(jsonString);
//            if (!json.isNull("uuid")) {
//                uuid = json.getString("uuid");
//            }
//            response.close();
//        } catch (ProcessingException | JSONException e) {
//            logger.error(e.getMessage());
//            throw new VegacenterException(ErrorMessages.getInstance().getMediaServerUnavailable());
//        }
//        return uuid;
//    }
//
//    /**
//     * uses the buildZipFilePath + getWorkingFilePath
//     *
//     * @param fileName
//     * @param machine
//     * @param runDirectory
//     * @return
//     */
//    private String zipDirectory(String fileName, String machine, String runDirectory) {
//        String zipFilePath = buildZipFilePath(fileName);
//        ZipUtil.pack(new File(getWorkingFilePath(machine, runDirectory, fileName)), new File(zipFilePath));
//        return zipFilePath;
//    }
//
//    private String buildZipFilePath(String fileName) {
//
//        String ZIP_FILES = this.getZipFileDirectory();
//        StringBuilder sb = new StringBuilder();
//        sb.append(ZIP_FILES);
//        sb.append(fileName);
//        sb.append(".zip");
//        return sb.toString();
//    }
//
//    private String getWorkingFilePath(String machine, String runDirName, String fileName) {
//        String baseWorkingPath = "";
//        if (machine.equals(this.getMachine())) {
//            baseWorkingPath = getWorkingLocalPath();
//        }
//        return baseWorkingPath + "/" + fileName;
//    }
//
//    private String getZipFileDirectory() {
//        final String ZIP_FILES = "/media/real-vegadare/vegadare-share/zipfiles/";
//        return ZIP_FILES;
//    }
//
//    private String getMachine() {
//        final String MACHINE_LOCAL = "Local-Machine";
//        return MACHINE_LOCAL;
//    }
//
//    private String getWorkingLocalPath() {
//        final String WORKING_LOCAL_PATH = "/media/real-vegadare/vegadare-share/workingfiles/Attom/Results";
//        return WORKING_LOCAL_PATH;
//    }
//
//    /**
//     *
//     * @param directory -> where does this come from ?
//     * @return
//     */
//    private String buildRequestString(String directory) {
//        FileInputStream is = null;
//        JSONObject jsonobject = new JSONObject();
//        try {
//            is = new FileInputStream(directory);
//            final byte[] bytes64bytes = Base64.encodeBase64(IOUtils.toByteArray(is));
//            final String content = new String(bytes64bytes);
//            jsonobject = new JSONObject();
//            jsonobject.put("owner", user.getFirstName());
//            jsonobject.put("access", "public");
//            jsonobject.put("licenseType", "CC BY");
//            jsonobject.put("fileName", "fileName");
//            jsonobject.put("fileDataBase64", content);
//
//        } catch (IOException ioex) {
//            System.out.println(ioex.getMessage());
//        } catch (JSONException ex) {
//            Logger.getLogger(Encoding64.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException ex) {
//
//            }
//        }
//        return StringEscapeUtils.unescapeJavaScript(jsonobject.toString());
//    }
//}
