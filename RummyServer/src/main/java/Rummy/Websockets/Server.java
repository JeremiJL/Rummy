package Rummy.Websockets;

import commons.Pair;
import commons.WebsocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Websocket server to handle connections with clients
 * For each game, a new one should be instantiated
 */
public class Server {
    private HashMap<InetAddress, ConnectionToClient> clientList;
    private LinkedBlockingQueue<Pair<InetAddress, WebsocketMessage>> messages;
    private ServerSocket serverSocket;

    public Server(int port, LinkedBlockingQueue<Pair<InetAddress, WebsocketMessage>> messages) {
        clientList = new HashMap<>();
        this.messages = messages;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Accepts new connection
        Thread accept = new Thread() {
            public void run(){
                while(true){
                    try{
                        Socket s = serverSocket.accept();
                        clientList.put(s.getInetAddress(), new ConnectionToClient(s));
                        System.out.println("Client connected");
                    }
                    catch(IOException e){ e.printStackTrace(); }
                }
            }
        };

        accept.setDaemon(true);
        accept.start();
    }

    private class ConnectionToClient {
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("initialized conn");

            // Accepts messages from a connection
            Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = in.readObject();
                            if (obj instanceof WebsocketMessage wm) {
                                messages.put(new Pair(socket.getInetAddress(), wm));
                            } else {
                                System.out.println("Received incorrect message type");
                            }

                        }
                        catch (SocketException e) {
                            break;
                        }
                        catch(IOException | ClassNotFoundException | InterruptedException e){ e.printStackTrace(); }
                    }
                }
            };

            read.setDaemon(true); // terminate when main ends
            read.start();
        }

        public void write(WebsocketMessage obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    public void sendToOne(InetAddress inet, WebsocketMessage message)  throws IndexOutOfBoundsException {
        clientList.get(inet).write(message);
    }

    public void sendToAll(WebsocketMessage message){
        for(ConnectionToClient client : clientList.values())
            client.write(message);
    }

}