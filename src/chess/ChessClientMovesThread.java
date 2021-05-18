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
            //Ako je dobio poruku od servera da je partija pocela
            if(opponentMoveReader.readBoolean()) {

                //Beli tim igra prvi
                if (parentApp.getClient().getTeam() == Team.WHITE) {

                    parentApp.setYourTurn(true);

                    //Ceka potez iz glavne aplikacije
                    try {
                        synchronized (parentApp.getMove()) {
                            parentApp.getMove().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int[] move = parentApp.getMove();
                    StringBuilder s = new StringBuilder("sending move: ");

                    //Salje potez na server
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

                while (true) {
                    parentApp.setYourTurn(false);
                    int oppX;
                    int oppY;
                    int oppXNEW;
                    int oppYNEW;

                    //Prima protivnicki potez za servera
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

                    //opponentMove() postavlja gameOver boolean na true ako se partija zavrsila sa primljenim potezom
                    if(parentApp.isGameOver()) {
                        break;
                    }

                    parentApp.setYourTurn(true);

                    //Ceka potez iz glavne aplikacije
                    try {
                        synchronized (parentApp.getMove()) {
                            parentApp.getMove().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int[] move = parentApp.getMove();
                    StringBuilder s = new StringBuilder("sending move: ");

                    //Salje potez na server
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

                    if(parentApp.isGameOver())
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
