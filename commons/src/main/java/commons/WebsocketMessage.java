package commons;

import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;

//GAME MODE MOVEMENTS:
//P->S Join lobby
//P->S, S->P Start game, finish game
//IN-GAME
//P->S Make Move
//S->P Move made (next_player, board_state)
//ERROR


/**
 * Parent of all messages sent over the network
 */
@Data
public abstract class WebsocketMessage implements Serializable {

}
