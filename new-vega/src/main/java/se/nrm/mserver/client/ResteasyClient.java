package se.nrm.mserver.client;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * For testing purposes, hardcoded file in the /tmp-directory. 2019-12-05:
 * https://www.baeldung.com/httpclient-redirect-on-http-post - 2019-11-05:
 * redirect i proxy fungerar när jag tar bort ./certs
 *
 * @author ingimar
 */
public class ResteasyClient {

    public static void main(String[] args) throws IOException {

        ResteasyClient thisClient = new ResteasyClient();
        thisClient.testPostBase64();
//        int status = thisClient.testGetBase64();
//        System.out.println("get response " + status);

    }

    /**
     * fetches the file itself.
     *
     * @return
     * @throws IOException
     */
    public int testGetBase64() throws IOException {
        int statusCode = 404;
        final String uri = "http://localhost:8080/mserver/media/v1/";
        String uuid = "1baf0d70-e53c-4f73-b973-17381e073ecf"; // får från POST
        String format = "?format=image/png";

        StringBuilder completeURI = new StringBuilder();
        completeURI.append(uri);
        completeURI.append(uuid);
        completeURI.append(format);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(completeURI.toString());

        CloseableHttpResponse response = httpClient.execute(request);
        statusCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            boolean streaming = entity.isStreaming();
            System.out.println("streaming ? " + streaming);
            String result = EntityUtils.toString(entity);
            // System.out.println(result);
        }

        return statusCode;
    }

    public void testPostBase64() throws IOException {
        System.out.println("start");

        String fileName = "testbild-svt-666.png";
        String filePath = "/tmp/".concat(fileName);

        CloseableHttpClient instance = HttpClientBuilder.create().build();

//        final String url = "http://media.nrm.se/mserver/media";
//        final String url = "http://localhost:8080/mserver/media";
        final String url = "https://api.nrm.se/mserver/media";
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        final File file = new File(filePath);
        FileBody fb = new FileBody(file);

        builder.addPart("content", fb);
        builder.addTextBody("owner", "Ingimar Erlingsson");
        builder.addTextBody("filename", fileName);

        final HttpEntity entity = builder.setLaxMode().build();

        post.setEntity(entity);
        HttpResponse response = instance.execute(post);
        HttpEntity responseeEntity = response.getEntity();

        // https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string-in-java
        String responseString = EntityUtils.toString(responseeEntity, "UTF-8");
        JSONObject result = new JSONObject(responseString);
        String uuid = (String)result.get("uuid");
     

        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("response " + responseString);
        System.out.println("\n response ".concat(Integer.toString(statusCode)));
        System.out.println("UUID " + uuid);

        instance.close(); // Httpclient fundamentals
    }
}
