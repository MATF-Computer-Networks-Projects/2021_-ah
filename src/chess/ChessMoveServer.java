package chess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessMoveServer {
    static final int MOVE_SERVER_PORT = 12345;

    private int port;

    private Socket[] players = new Socket[2];

    public ChessMoveServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        ChessMoveServer server = new ChessMoveServer(MOVE_SERVER_PORT);
        server.execute();
    }

    private void execute() {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            for(int i = 0; i<1; i++){
                Socket socket = serverSocket.accept();
                players[i] = socket;

                System.err.println("Connected player "+(i+1));
                socket.getOutputStream().write(i+1);
            }

            int i = 0;
            int x;
            int y;
            int xnew;
            int ynew;
            while(true){
                DataInputStream inStream = new DataInputStream(players[i].getInputStream());
                x = inStream.readInt();
                y = inStream.readInt();
                xnew = inStream.readInt();
                ynew = inStream.readInt();
                System.err.println("move recieved "+x+" "+y+" "+xnew+" "+ynew+" from player "+(1-i));

                DataOutputStream outStream = new DataOutputStream(players[1-i].getOutputStream());
                outStream.writeInt(x);
                outStream.writeInt(y);
                outStream.writeInt(xnew);
                outStream.writeInt(ynew);
                i = 1-i;
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
