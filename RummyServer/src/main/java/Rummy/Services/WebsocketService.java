package Rummy.Services;

import Rummy.Websockets.Server;
import commons.ExampleWebsocketMessage;
import commons.WebsocketMessage;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * Service layer to abstract away the websocketserver
 * Could be used to, for example, send message to a particular username, current player, etc.
 */
@Service
public class WebsocketService {

    public void sendMessageToAll(Server server, WebsocketMessage message) {
        server.sendToAll(message);
    }

    //TODO: OVERLOAD WITH PLAYER USERNAME
    public void sendToOneUsername(Server server, String username, WebsocketMessage message) {

    }

    public void sendToOne(Server server, InetAddress inet, WebsocketMessage message)  throws IndexOutOfBoundsException {
        server.sendToOne(inet, message);
    }

}
