package commons;

import lombok.Data;

import java.net.InetAddress;

@Data
public class ExampleWebsocketMessage extends WebsocketMessage {

    private final Player fromPlayer;
}
