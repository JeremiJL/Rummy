package RummyClient.Communication;

import commons.WebsocketMessage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WebsocketService {

    public static WebsocketClient initializeConnection(String ip, int port, LinkedBlockingQueue<WebsocketMessage> messages) {
        try {
            return new WebsocketClient(ip, port, messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessage(WebsocketClient client, WebsocketMessage message) {
        client.send(message);}

    //TODO
    public static void handleMessage() {

    }
}
