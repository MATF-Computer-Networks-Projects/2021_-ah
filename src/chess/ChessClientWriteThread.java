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
            if (ke.getCode().equals(KeyCode.ENTER)) {
                int i = team.equals(Team.WHITE) ? 1 : 2;
                Text text = new Text("Player "+i+":\n"+textField.getText()+"\n");
                textFlow.getChildren().add(text);
                if(textField.getText().equals("cao"))
                    textFlow.getChildren().add(new Text("Player "+i+" has disconnected\n"));
                messageWriter.println(textField.getText());
                textField.clear();
            }
        });



    }
}
