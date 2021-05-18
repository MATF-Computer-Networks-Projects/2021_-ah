package chess;

import java.io.*;
import java.net.Socket;

public class ChessClientMovesThread extends Thread{
    private DataInputStream opponentMoveReader;
    private DataOutputStream moveSender;
    ChessClientApplication parentApp;

    public ChessClientMovesThread(Socket socket, ChessClientApplication parentApp) {

        this.parentApp = parentApp;

        try {
            this.opponentMoveReader = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.moveSender = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            if(opponentMoveReader.readBoolean()) {

                if (parentApp.getClient().getTeam() == Team.WHITE) {

                    parentApp.setYourTurn(true);

                    try {
                        synchronized (parentApp.getMove()) {
                            parentApp.getMove().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int[] move = parentApp.getMove();
                    StringBuilder s = new StringBuilder("sending move: ");

                    for (int a : move) {
                        try {
                            moveSender.writeInt(a);
                            s.append(a);
                            s.append(" ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(s);
                }
                parentApp.setYourTurn(false);
                while (true) {
                    int oppX;
                    int oppY;
                    int oppXNEW;
                    int oppYNEW;
                    try {
                        oppX = opponentMoveReader.readInt();
                        oppY = opponentMoveReader.readInt();
                        oppXNEW = opponentMoveReader.readInt();
                        oppYNEW = opponentMoveReader.readInt();
                        System.out.println("move recieved " + oppX + " " + oppY + " " + oppXNEW + " " + oppYNEW);
                        parentApp.opponentMove(oppX, oppY, oppXNEW, oppYNEW);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if(parentApp.isGameOver()) {
                        break;
                    }

                    parentApp.setYourTurn(true);
                    try {
                        synchronized (parentApp.getMove()) {
                            parentApp.getMove().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int[] move = parentApp.getMove();
                    StringBuilder s = new StringBuilder("sending move: ");

                    for (int a : move) {
                        try {
                            moveSender.writeInt(a);
                            s.append(a);
                            s.append(" ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(s);
                    parentApp.setYourTurn(false);

                    if(parentApp.isGameOver())
                        break;
                }
            }
            System.out.println("exited thread");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
