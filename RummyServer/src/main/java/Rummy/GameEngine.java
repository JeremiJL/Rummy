package Rummy;

import Rummy.Services.WebsocketService;
import Rummy.Websockets.Server;
import commons.ExampleWebsocketMessage;
import commons.Pair;
import commons.Player;
import commons.WebsocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Primary game controller
 * Keeps track of the game state, processes user communication
 * TODO: Handling multiple games at once
 */
@Component
public class GameEngine {
    private final Server websocketServer;
    private final LinkedBlockingQueue<Pair<InetAddress, WebsocketMessage>> messages;
    private final WebsocketService websocketService;

    @Autowired
    public GameEngine(WebsocketService websocketService) {
        this.websocketService = websocketService;
        messages = new LinkedBlockingQueue<Pair<InetAddress, WebsocketMessage>>();
        websocketServer = new Server(8090, messages);
        startMessageHandlingLoop();
    }


    /**
     * Handles messages coming in from the WebsocketServer
     * Should be in a lower layer, but I can't be bothered to deal with EventHandlers rn
     */
     public void startMessageHandlingLoop() {
        Thread messageHandling = new Thread() {
            public void run(){
                while(true){
                    try{
                        Pair<InetAddress, WebsocketMessage> message = messages.take();
                        System.out.println("Received message on the loopyloop: " + message);
                        handleMessage(message);

                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    /**
     * Initial processing of requests
     * Calls correct methods based on received message type
     * @param messagePair
     */
    private void handleMessage(Pair<InetAddress, WebsocketMessage> messagePair) {
        InetAddress inetAddress = messagePair.first;
        WebsocketMessage wm = messagePair.second;
        if (wm instanceof ExampleWebsocketMessage ewm) handleExampleMessage(inetAddress, ewm);
        else {
            System.out.println("RECEIVED UNKNOWN MESSAGE TYPE" + wm.getClass());
        }
    }

    private void handleExampleMessage(InetAddress inetAddress, ExampleWebsocketMessage ewm) {
        System.out.println("Received ping!");
        System.out.println("ID: " + ewm.getFromPlayer().getId());
        websocketService.sendToOne(websocketServer,
            inetAddress,
            new ExampleWebsocketMessage(new Player("server")));

        websocketService.sendMessageToAll(websocketServer,
            new ExampleWebsocketMessage(new Player("serverToAll")));
    }
}