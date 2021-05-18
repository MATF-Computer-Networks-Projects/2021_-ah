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

    private final int port;

    private PlayerOnServer[] players = new PlayerOnServer[2];

    public ChessMoveServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        ChessMoveServer server = new ChessMoveServer(MOVE_SERVER_PORT);
        server.execute();
    }

    private void execute() {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            //Prima dva korisnika i salje im broj kojim odredjuju tim u zavisnosti od toga koji se prvi povezao
            for(int i = 0; i<2; i++){
                Socket socket = serverSocket.accept();
                players[i] = new PlayerOnServer(socket);

                System.err.println("Connected player "+(i+1));

                players[i].getOstream().writeInt(i+1);
                //Kada primi drugog korisnik, salje im poruku da je partija pocela
                if(i==1){
                    for(int j=0; j<2; j++)
                        players[j].getOstream().writeBoolean(true);
                }

            }
            //Indeks korisnika, krece od belog
            int i = 0;

            int x;
            int y;
            int xnew;
            int ynew;
            //
            while(true){
                //Prima potez jednog korisnika i salje ga drugom
                DataInputStream inStream = players[i].getIstream();
                x = inStream.readInt();
                y = inStream.readInt();
                xnew = inStream.readInt();
                ynew = inStream.readInt();

                System.err.println("move recieved "+x+" "+y+" "+xnew+" "+ynew+" from player "+(1+i));

                DataOutputStream outStream = players[1-i].getOstream();
                outStream.writeInt(x);
                outStream.writeInt(y);
                outStream.writeInt(xnew);
                outStream.writeInt(ynew);
                //Promena aktivnog korisnika
                i = 1-i;
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
