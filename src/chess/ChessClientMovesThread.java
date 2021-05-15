package chess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChessClientMovesThread extends Thread{
    private BufferedInputStream opponentMoveReader;
    private BufferedOutputStream moveSender;
    int x;
    int y;
    int xnew;
    int ynew;

    public ChessClientMovesThread(Socket socket) {
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

    public void setMoveCoordinates(int x, int y, int xnew, int ynew){
        this.x = x;
        this.y = y;
        this.xnew = xnew;
        this.ynew = ynew;
    }
}
