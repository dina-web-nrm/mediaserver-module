package se.nrm.bio.mediaserver.clients;

import org.apache.log4j.Logger;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * Local fetch : 
 *    http://127.0.0.1:8080/MediaServerResteasy/media/ef1311b1-9bd3-40ea-a794-b124d796024a?format=image/jpeg
 *    
 *    Version 1.
 *    http://127.0.0.1:8080/MediaServerResteasy/media/v1/ef1311b1-9bd3-40ea-a794-b124d796024a
 * 
 *    Version 2.
 *    http://127.0.0.1:8080/MediaServerResteasy/media/v2/ef1311b1-9bd3-40ea-a794-b124d796024a?format=image/jpeg
 * 
 * @author ingimar
 */
public class ClientEncodedUpgrade {
//    final static Logger logger = Logger.getLogger(ClientEncodedUpgrade.class);

    private final String MEDIA_SERVER_URL;
    private ResteasyClient client;
    private ResteasyWebTarget target;

    public ClientEncodedUpgrade() {
        this.MEDIA_SERVER_URL = Util.getLocalURL();
        System.out.println("URL is ".concat(MEDIA_SERVER_URL));
    }

    public static void main(String[] args) {
        ClientEncodedUpgrade client = new ClientEncodedUpgrade();
        String uuid = "ef1311b1-9bd3-40ea-a794-b124d796024a";
        Response response = client.fetchFileFromMediaserver(uuid);
        int status = response.getStatus();

        System.out.println("Status is ".concat(Integer.toString(status)));
    }

    public Response fetchFileFromMediaserver(String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MEDIA_SERVER_URL).append("/");
        sb.append(uuid);
        sb.append("?format=application/zip");

        System.out.println("Concatenated string is ".concat(sb.toString()));
        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        client = clientBuilder.build();
        target = client.target(sb.toString());
        Response response = target.request().get();
        return response;
    }

}
