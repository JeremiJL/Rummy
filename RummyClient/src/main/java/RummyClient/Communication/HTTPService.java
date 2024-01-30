package RummyClient.Communication;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * LEGACY
 * Probably unnecessary, used for HTTP connections
 * In case we ever decide to use those
 */
public class HTTPService {

    private static final Client client = ClientBuilder.newClient(new ClientConfig());
    private static String server = "http://192.168.0.104:8080";

    public static Response getRequest(String path) {
//        URL url = new URL(Constants.SERVER_IP + path)
        Response res = client.target(getServerIp()).path(path)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Response.class);
        return res;
    }

    public static <T> Response postRequest(String path, T data) {
//        URL url = new URL(Constants.SERVER_IP + path)
        Response res = client.target(Constants.SERVER_IP).path(path)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(data, APPLICATION_JSON), Response.class);
        return res;
    }

    private static String getServerIp() {
        return "http://" + Constants.SERVER_IP + ":" + Constants.HTTP_PORT + "/";
    }

}
