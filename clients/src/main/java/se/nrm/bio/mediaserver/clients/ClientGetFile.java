package se.nrm.bio.mediaserver.clients;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * INTRO <p> 
 * Make sure that the UUID 'ef1311b1-9bd3-40ea-a794-b124d796024a'
 * <p>
 * is in the database&filesystem
 * <p>
 * mysql> select * from MEDIA where UUID='ef1311b1-9bd3-40ea-a794-b124d796024a';
 *
 * @author ingimar
 */
public class ClientGetFile {

    public static void main(String[] args) throws IOException {
        ClientGetFile client = new ClientGetFile();
        CloseableHttpResponse response = client.getFileFromMediaServer("ef1311b1-9bd3-40ea-a794-b124d796024a");
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println(response.getProtocolVersion());              // HTTP/1.1
        System.out.println(response.getStatusLine().getStatusCode());   // 200
        System.out.println(response.getStatusLine().getReasonPhrase()); // OK
        System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

    }

    /**
     * using version 2 of the API
     * <p>
     * using the format "image/jpeg"
     * <p>
     *
     * @param UUID
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse getFileFromMediaServer(String UUID) throws IOException {
        String VERSION = "/v2/";
        String FORMAT = "image/jpeg";
        String URL = Util.getLocalURL().concat(VERSION).concat(UUID).concat("?format=").concat(FORMAT);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URL);
            try (CloseableHttpResponse response = httpClient.execute(request)) {

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    //System.out.println(result);
                }
                return response;
            }
        }

    }

}
