package chess;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChessClientWriteThread extends Thread{
    private PrintWriter messageWriter;
    private TextFlow textFlow;
    private TextField textField;
    private Team team;

    public ChessClientWriteThread(Socket socket, TextFlow textFlow, TextField textField, Team team){
        try {
            messageWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textFlow = textFlow;
        this.textField = textField;
        this.team = team;
    }

    public void run() {

        textField.setOnKeyPressed(ke -> {

            //Na pritisak tastera ENTER salje poruku iz TextField-a na server i ispisuje je lokalno u prozoru
            if (ke.getCode().equals(KeyCode.ENTER)) {

                //Ispis se menja u zavisnosti od tima
                int i = team.equals(Team.WHITE) ? 1 : 2;
                Text text = new Text("Player "+i+":\n"+textField.getText()+"\n");
                textFlow.getChildren().add(text);

                //Ako je poslao "cao" zaustavlja rad
                if(textField.getText().equals("cao"))
                    textFlow.getChildren().add(new Text("Player "+i+" has disconnected\n"));
                
                messageWriter.println(textField.getText());
                textField.clear();
            }
        });



    }
}
