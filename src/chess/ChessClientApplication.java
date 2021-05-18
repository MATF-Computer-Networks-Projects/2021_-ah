package chess;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChessClientApplication extends Application implements PropertyChangeListener {
    private ChessClient client;

    private ChessClientMovesThread moveThread;

    private boolean buttonSelected = false;
    private boolean yourTurn = false;
    private boolean gameOver = false;


    Map<String, Image> images = new HashMap<>();

    private ChessBoard board;

    int[] move = new int[4];


    private GridPane pane;

    private Label turnLabel = new Label();


    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {

        client = new ChessClient("localhost");

        Socket chessSocket = new Socket(client.getHostname(), ChessMoveServer.MOVE_SERVER_PORT);

        getTeam(chessSocket);

        Socket chatSocket = new Socket(client.getHostname(), ChessChatServer.CHAT_SERVER_PORT);


        TextFlow textflow = new TextFlow();

        ScrollPane sp = new ScrollPane();

        TextField message = new TextField();

        pane = new GridPane();

        pane.setGridLinesVisible(true);

        board = new ChessBoard();
        board.addPropertyChangeListener(this);

        loadImages();

//        pane.setPadding(new Insets(50, 50, 50, 50));
        pane.setVgap(2);
        pane.setHgap(2);

        initialize();
        VBox chatBox = initializeChat(textflow, sp, message);

        moveThread = new ChessClientMovesThread(chessSocket, this);
        ChessClientReadThread chatReadThread = new ChessClientReadThread(chatSocket, textflow, client.getTeam());
        ChessClientWriteThread chatWriteThread = new ChessClientWriteThread(chatSocket, textflow, message, client.getTeam());

        VBox chessBox = new VBox();
        chessBox.setPadding(new Insets(50, 50, 50, 50));
        chessBox.setAlignment(Pos.CENTER);
        turnLabel.setFont(new Font("Arial", 15));
        turnLabel.setPadding(new Insets(0, 0, 15, 0));
        chessBox.getChildren().add(turnLabel);
        chessBox.getChildren().add(pane);
        HBox hbox = new HBox(chessBox, chatBox);

        Scene scene = new Scene(hbox);
        stage.setScene(scene);

        stage.show();

        moveThread.start();
        chatReadThread.start();
        chatWriteThread.start();
    }

    private VBox initializeChat(TextFlow textflow, ScrollPane sp, TextField message) {
        VBox chat = new VBox();
        chat.getChildren().add(textflow);
        sp.setFitToHeight(true);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setPrefSize(200, 520);
        sp.setStyle("-fx-background-color: black");
        sp.setContent(chat);
        message.setPrefSize(200, 20);
        VBox chatBox = new VBox();
        chatBox.getChildren().add(0, message);
        chatBox.getChildren().add(0, sp);
        chatBox.setPadding(new Insets(50, 20, 50, 0));
        chatBox.setPrefSize(200, 480);
        return chatBox;
    }


    private void initialize(){
        boolean grey = true;

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                Button button = new Button("");
                button.setPrefSize(60, 60);
                button.setPadding(new Insets(0, 0, 0, 0));


                if(i<2 || i>5){
                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
                    String s = board.getPiece(j, i).getImgFile() + ".png";
                    Image image = images.get(s);
                    ImageView imageView = new ImageView(image);
                    button.setGraphic(imageView);
                }




                if (grey)
                    button.setStyle("-fx-background-color: Grey");
                else
                    button.setStyle("-fx-background-color: White");

                if(client.getTeam()==Team.WHITE)
                    pane.add(button, j, 7-i);
                else
                    pane.add(button, 7-j, i);



                button.setOnMouseClicked( e -> {

                    //Biranje prvog polja u potezu
                    if(!buttonSelected && yourTurn){

                        if(client.getTeam()==Team.WHITE){
                            move[0] = GridPane.getColumnIndex(button);
                            move[1] = 7 - GridPane.getRowIndex(button);
//                            x = GridPane.getColumnIndex(button);
//                            y = 7-GridPane.getRowIndex(button);
                        }
                        else{
                            move[0] = 7 - GridPane.getColumnIndex(button);
                            move[1] = GridPane.getRowIndex(button);
//                            x = 7-GridPane.getColumnIndex(button);
//                            y = GridPane.getRowIndex(button);
                        }

                        if(board.getPiece(move[0], move[1])==null)
                            System.out.println("No piece");
                        else if(board.getPiece(move[0], move[1]).getTeam()!=client.getTeam())
                            System.out.println("Wrong team");
                        else {
                            System.out.println(board.getPiece(move[0], move[1]).getType());
                            buttonSelected = !buttonSelected;
                        }
                    }
                    //Biranje drugog polja i pomeranje ako je dozvoljeno
                    else if(buttonSelected && yourTurn){
                        if(client.getTeam()==Team.WHITE){
                            move[2] = GridPane.getColumnIndex(button);
                            move[3] = 7-GridPane.getRowIndex(button);
//                            xnew = GridPane.getColumnIndex(button);
//                            ynew = 7-GridPane.getRowIndex(button);
                        }
                        else{
                            move[2] = 7-GridPane.getColumnIndex(button);
                            move[3] = GridPane.getRowIndex(button);
//                            xnew = 7-GridPane.getColumnIndex(button);
//                            ynew = GridPane.getRowIndex(button);
                        }

                        if(board.getPiece(move[0], move[1]).move(board, move[2], move[3])){

                            //Prebacuje sliku figure na nove koordinate
//                            moveThread.setMoveCoordinates(x, y, xnew, ynew);
//                            moveThread.notify();
//                            moveInUI(x, y, xnew, ynew);
                            synchronized (move) {
                                move.notify();
                            }
                        }else{
                            System.out.println("illegal move!");
                        }
                        buttonSelected = !buttonSelected;
                    }
                });
                grey = !grey;
            }
            grey = !grey;
        }
    }




//    private void moveInUI(int x, int y, int xnew, int ynew){
//        for(Node node : pane.getChildren()) {
//            if(client.getTeam()==Team.WHITE) {
//                if (node instanceof Button && GridPane.getColumnIndex(node).equals(x) && GridPane.getRowIndex(node).equals(7 - y)) {
//
//                    ((Button) node).setGraphic(null);
//                } else if (node instanceof Button && GridPane.getColumnIndex(node).equals(xnew) && GridPane.getRowIndex(node).equals(7 - ynew)) {
//
//                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
//                    String s = board.getPiece(xnew, ynew).getImgFile() + ".png";
//                    Image image = images.get(s);
//                    ImageView imageView = new ImageView(image);
//                    ((Button) node).setGraphic(imageView);
//                }
//            }
//            else {
//                if (node instanceof Button && GridPane.getColumnIndex(node).equals(7-x) && GridPane.getRowIndex(node).equals(y)) {
//
//                    ((Button) node).setGraphic(null);
//                } else if (node instanceof Button && GridPane.getColumnIndex(node).equals(7-xnew) && GridPane.getRowIndex(node).equals(ynew)) {
//
//                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
//                    String s = board.getPiece(xnew, ynew).getImgFile() + ".png";
//                    Image image = images.get(s);
//                    ImageView imageView = new ImageView(image);
//                    ((Button) node).setGraphic(imageView);
//                }
//            }
//        }
//    }

    private void loadImages() {

        File imageDirectory = new File("M:\\Projects\\2021_Sah\\icons");
        String[] imageKeys = imageDirectory.list();
        assert imageKeys != null;
        for(String image : imageKeys) {
            try {
                images.put(image, new Image(new FileInputStream("M:\\Projects\\2021_Sah\\icons\\"+image)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    private void getTeam(Socket socket){
        try {
            int team = new DataInputStream(socket.getInputStream()).readInt();
            if(team == 1)
                this.client.setTeam(Team.WHITE);
            else
                this.client.setTeam(Team.BLACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Proverava promene na backend sahovskoj tabli i replicira promene na GUI tabli
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        //Ako je nova vrednost null, to je prethodno polje u potezu
        if(evt.getNewValue() == null) {
            for(Node node : pane.getChildren()) {

                //Koordinate za prikaz table belom timu
                if(client.getTeam()==Team.WHITE
                    && node instanceof Button
                    && GridPane.getColumnIndex(node).equals(((ChessPiece) evt.getOldValue()).getX())
                    && GridPane.getRowIndex(node).equals(7 - ((ChessPiece) evt.getOldValue()).getY())) {
                        ((Button) node).setGraphic(null);

                //Koordinate za prikaz table crnom timu
                } else if (client.getTeam()==Team.BLACK
                    && node instanceof Button
                    && GridPane.getColumnIndex(node).equals(7 - ((ChessPiece) evt.getOldValue()).getX())
                    && GridPane.getRowIndex(node).equals(((ChessPiece) evt.getOldValue()).getY())){
                        ((Button) node).setGraphic(null);
                }
            }

        //Sledece polje u potezu
        } else {
            if(evt.getOldValue() != null && ((ChessPiece) evt.getOldValue()).getType().equals("king")){
                gameOver = true;
                if(((ChessPiece) evt.getOldValue()).getTeam().equals(Team.BLACK))
                    System.out.println("White wins");
                else
                    System.out.println("Black wins");
            }
            for(Node node : pane.getChildren()) {

                //Koordinate za prikaz table belom timu
                if(client.getTeam()==Team.WHITE
                        && node instanceof Button
                        && GridPane.getColumnIndex(node).equals(((ChessPiece) evt.getNewValue()).getX())
                        && GridPane.getRowIndex(node).equals(7 - ((ChessPiece) evt.getNewValue()).getY())) {
                            String s = ((ChessPiece) evt.getNewValue()).getImgFile() + ".png";
                            Image image = images.get(s);
                            ImageView imageView = new ImageView(image);
                            ((Button) node).setGraphic(imageView);

                //Koordinate za prikaz table crnom timu
                } else if (client.getTeam()==Team.BLACK
                        && node instanceof Button
                        && GridPane.getColumnIndex(node).equals(7 - ((ChessPiece) evt.getNewValue()).getX())
                        && GridPane.getRowIndex(node).equals(((ChessPiece) evt.getNewValue()).getY())){
                            String s = ((ChessPiece) evt.getNewValue()).getImgFile() + ".png";
                            Image image = images.get(s);
                            ImageView imageView = new ImageView(image);
                            ((Button) node).setGraphic(imageView);
                }
            }

        }
    }

    public ChessClient getClient() {
        return client;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
        Platform.runLater(() -> {
            if(yourTurn)
                turnLabel.setText("YOUR TURN");
            else
                turnLabel.setText("OPPONENT'S TURN");
        });
    }

    public int[] getMove() {
        return move;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    //    public int[] getMove(){
//        return new int[]{x, y, xnew, ynew};
//    }

    public void opponentMove(int x, int y, int xnew, int ynew){


        if(board.getPiece(xnew, ynew) != null && board.getPiece(xnew, ynew).getType().equals("king"))
            gameOver = true;
        //Metoda se pokrece samo iz moveThread niti, pa je potrebno dodati runLater jer menja GUI
        Platform.runLater(() -> board.getPiece(x, y).move(board, xnew, ynew));

    }

//    public void receiveMessage(Text message){
//        Platform.runLater(new Runnable(){
//            public void run() {
//                textFlow.getChildren().add(new Text("Player 2: \n"+message+"\n"));
//            }
//        });
//    }
}
