package chess;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChessClientReadThread extends Thread{
    private BufferedReader messageReader;
    private TextFlow textflow;
    private Team team;

    public ChessClientReadThread(Socket socket, TextFlow textflow, Team team){
        try {
            messageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textflow = textflow;
        this.team = team;
    }

    public void run() {
        while (true) {
            try {

                //Prima poruku sa servera
                String opponentMessage = messageReader.readLine();

                if (opponentMessage == null) {
                    System.err.println("\rConnection lost.");
                    return;
                }

                //Ispis se menja u zavisnosti od tima
                int i = team.equals(Team.WHITE) ? 2 : 1;

                //Ispisuje primljenu poruku u prozoru
                Platform.runLater(() -> textflow.getChildren().add(new Text("Player "+i+":\n"+opponentMessage+"\n")));

                //Ako je primio "cao" zaustavlja rad
                if(opponentMessage.equals("cao")) {
                    Platform.runLater(() -> textflow.getChildren().add(new Text("Player " + i + " has disconnected\n")));
                    break;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
