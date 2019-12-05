package se.nrm.mserver.client;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * For testing purposes, hardcoded file in the /tmp-directory. 2019-12-05:
 * https://www.baeldung.com/httpclient-redirect-on-http-post
 * - 2019-11-05: redirect i proxy fungerar när jag tar bort ./certs
 *
 * @author ingimar
 */
public class ResteasyClient {

    public static void main(String[] args) throws IOException {
        System.out.println("start");

        String fileName = "testbild-svt-666.png";
        String filePath = "/tmp/".concat(fileName);

//        CloseableHttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        CloseableHttpClient instance = HttpClientBuilder.create().build();

//        final String url = "http://localhost:8080/MediaServerResteasy/rest/file/vega";
        final String url = "http://media.nrm.se/mserver/rest/file/vega";
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        final File file = new File(filePath);
        FileBody fb = new FileBody(file);

        builder.addPart("content", fb);
        builder.addTextBody("owner", "Ingimar Erlingsson");
        builder.addTextBody("filename", fileName);
//        builder.addTextBody("licenseType", "CC-BY");

        final HttpEntity entity = builder.setLaxMode().build();

        post.setEntity(entity);
        HttpResponse response = instance.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("\n response ".concat(Integer.toString(statusCode)));
        System.out.println("response ");

        instance.close(); // Httpclient fundamentals
    }
}
