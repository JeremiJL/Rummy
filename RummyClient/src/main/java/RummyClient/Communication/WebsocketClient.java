package RummyClient.Communication;

import commons.WebsocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class WebsocketClient {
    private ConnectionToServer server;
    private Socket socket;
    private LinkedBlockingQueue<WebsocketMessage> messages;

    public WebsocketClient(String IPAddress, int port, LinkedBlockingQueue<WebsocketMessage> messages) throws IOException{
        socket = new Socket(IPAddress, port);
        server = new ConnectionToServer(socket);
        this.messages = messages;
    }

    private class ConnectionToServer {
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = in.readObject();
                            if (obj instanceof WebsocketMessage wm) {
                                messages.put(wm);
                            } else {
                                throw new RuntimeException("Received incorrect message type");
                            }
                        }
                        catch (SocketException e) {
                            break;
                        }
                        catch(IOException | InterruptedException | ClassNotFoundException e){ e.printStackTrace(); }
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }

        private void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ throw new RuntimeException(); }
        }


    }

    public void send(Object obj) {
        server.write(obj);
    }

    public InetAddress getInetAdress() {
        return socket.getInetAddress();
    }
}