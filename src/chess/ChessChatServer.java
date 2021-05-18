package chess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessChatServer {
    static final int CHAT_SERVER_PORT = 12346;

    public static void main(String[] args) {
        ChessChatServer server = new ChessChatServer(CHAT_SERVER_PORT);
        server.execute();
    }

    private int port;
    private ChatUserThread[] users = new ChatUserThread[2];

    private ChessChatServer(int port) {
        this.port = port;
    }

    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            for(int i = 0; i<2; i++){
                Socket socket = serverSocket.accept();
                users[i] = new ChatUserThread(socket, this, i);

                System.err.println("Connected player "+(i+1));
                users[i].start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void broadcast(String clientMessage, int index) {
        users[1-index].sendMessage(clientMessage);
    }



}
