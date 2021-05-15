package chess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChessClientMovesThread extends Thread{
    ChessClient client;
    private BufferedInputStream opponentMoveReader;
    private BufferedOutputStream moveSender;

    public ChessClientMovesThread(ChessClient client, Socket socket) {
        this.client = client;
        try {
            this.opponentMoveReader = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.moveSender = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
