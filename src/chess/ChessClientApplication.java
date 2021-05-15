package chess;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChessClientApplication extends Application implements PropertyChangeListener {
    private ChessClient client;

    private ChessClientMovesThread moveThread;

    private boolean buttonSelected = false;

    Map<String, Image> images = new HashMap<>();

    ChessBoard board;

    int x;
    int y;
    int xnew;
    int ynew;

    GridPane pane;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {

        client = new ChessClient("localhost", 12345);

//        Socket socket = new Socket(client.getHostname(), client.getPort());
//
//        getTeam(socket);
        client.setTeam(Team.WHITE);

        pane = new GridPane();

        pane.setGridLinesVisible(true);

        board = new ChessBoard();
        board.addPropertyChangeListener(this);
        loadImages();

        pane.setPadding(new Insets(50, 50, 50, 50));
        pane.setVgap(2);
        pane.setHgap(2);

        initialize();

//        moveThread = new ChessClientMovesThread(socket);
//
//        moveThread.start();

        Scene scene = new Scene(pane);
        stage.setScene(scene);

        stage.show();
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
                    if(!buttonSelected){

                        if(client.getTeam()==Team.WHITE){
                            x = GridPane.getColumnIndex(button);
                            y = 7-GridPane.getRowIndex(button);
                        }
                        else{
                            x = 7-GridPane.getColumnIndex(button);
                            y = GridPane.getRowIndex(button);
                        }

                        if(board.getPiece(x, y)==null)
                            System.out.println("No piece");
                        else if(board.getPiece(x, y).getTeam()!=client.getTeam())
                            System.out.println("Wrong team");
                        else {
                            System.out.println(board.getPiece(x, y).getType());
                            buttonSelected = !buttonSelected;
                        }
                    }
                    //Biranje drugog polja i pomeranje ako je dozvoljeno
                    else {
                        if(client.getTeam()==Team.WHITE){
                            xnew = GridPane.getColumnIndex(button);
                            ynew = 7-GridPane.getRowIndex(button);
                        }
                        else{
                            xnew = 7-GridPane.getColumnIndex(button);
                            ynew = GridPane.getRowIndex(button);
                        }

                        if(board.getPiece(x, y).move(board, xnew, ynew)){

                            //Prebacuje sliku figure na nove koordinate
//                            moveThread.setMoveCoordinates(x, y, xnew, ynew);
//                            moveThread.notify();
//                            moveInUI(x, y, xnew, ynew);
                            System.out.println(board);
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
        for(String image : imageKeys) {
            try {
                System.out.println(image);
                images.put(image, new Image(new FileInputStream("M:\\Projects\\2021_Sah\\icons\\"+image)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isButtonSelected() {
        return buttonSelected;
    }

    public void setButtonSelected(boolean buttonSelected) {
        this.buttonSelected = buttonSelected;
    }



    private void getTeam(Socket socket){
        try {
            Scanner sc = new Scanner(socket.getInputStream());
            int team = sc.nextInt();
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

}
