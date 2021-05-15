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
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChessClientApplication extends Application {
    private ChessClient client;


    private boolean buttonSelected = false;
    private boolean moveSelected = false;

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

        Socket socket = new Socket(client.getHostname(), client.getPort());

        getTeam(socket);
        pane = new GridPane();

        pane.setGridLinesVisible(true);

        this.board = new ChessBoard();
        this.board.restartPositions();
        loadImages();

        pane.setPadding(new Insets(50, 50, 50, 50));
        pane.setVgap(2);
        pane.setHgap(2);

        if(client.getTeam()==Team.WHITE)
            initializeP1();
        else
            initializeP2();

        Scene scene = new Scene(pane);
        stage.setScene(scene);

        stage.show();
    }



    private void initializeP1(){
        boolean grey = true;

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                Button button = new Button("");
                button.setPrefSize(60, 60);
                button.setPadding(new Insets(0, 0, 0, 0));


                if(i<2 || i>5){
                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
                    String s = this.board.getPiece(j, i).getImgFile() + ".png";
                    Image image = images.get(s);
                    ImageView imageView = new ImageView(image);
                    button.setGraphic(imageView);
                }




                if (grey)
                    button.setStyle("-fx-background-color: Grey");
                else
                    button.setStyle("-fx-background-color: White");

                this.pane.add(button, j, 7-i);



                button.setOnMouseClicked( e -> {

                    //Biranje prvog polja u potezu
                    if(!buttonSelected && !moveSelected){

                        x = GridPane.getColumnIndex(button);
                        y = 7-GridPane.getRowIndex(button);
                        buttonSelected = !buttonSelected;

                    }
                    //Biranje drugog polja i pomeranje ako je dozvoljeno
                    else if(buttonSelected && !moveSelected){
                        xnew = GridPane.getColumnIndex(button);
                        ynew = 7-GridPane.getRowIndex(button);


                        if(this.board.getPiece(x, y).move(this.board, xnew, ynew)){

                            //Prebacuje sliku figure na nove koordinate
                            localMove(x, y, xnew, ynew);

                        }else{
                            System.out.println("illegal move!");
                        }
                        moveSelected = !moveSelected;
                    }
                });
                grey = !grey;
            }
            grey = !grey;
        }
    }


    private void initializeP2(){
        boolean grey = true;

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                Button button = new Button("");
                button.setPrefSize(60, 60);
                button.setPadding(new Insets(0, 0, 0, 0));


                if(i<2 || i>5){
                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
                    String s = this.board.getPiece(j, i).getImgFile() + ".png";
                    Image image = images.get(s);
                    ImageView imageView = new ImageView(image);
                    button.setGraphic(imageView);
                }




                if (grey)
                    button.setStyle("-fx-background-color: Grey");
                else
                    button.setStyle("-fx-background-color: White");

                this.pane.add(button, 7-j, i);



                button.setOnMouseClicked( e -> {

                    //Biranje prvog polja u potezu
                    if(!buttonSelected){

                        x = 7-GridPane.getColumnIndex(button);
                        y = GridPane.getRowIndex(button);
                        buttonSelected = !buttonSelected;

                    }
                    //Biranje drugog polja i pomeranje ako je dozvoljeno
                    else{
                        xnew = 7-GridPane.getColumnIndex(button);
                        ynew = GridPane.getRowIndex(button);


                        if(this.board.getPiece(x, y).move(this.board, xnew, ynew)){

                            //Prebacuje sliku figure na nove koordinate
                            localMove(x, y, xnew, ynew);

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




    private void localMove(int x, int y, int xnew, int ynew){
        for(Node node : this.pane.getChildren()) {
            if(client.getTeam()==Team.WHITE) {
                if (node instanceof Button && GridPane.getColumnIndex(node).equals(x) && GridPane.getRowIndex(node).equals(7 - y)) {

                    ((Button) node).setGraphic(null);
                } else if (node instanceof Button && GridPane.getColumnIndex(node).equals(xnew) && GridPane.getRowIndex(node).equals(7 - ynew)) {

                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
                    String s = this.board.getPiece(xnew, ynew).getImgFile() + ".png";
                    Image image = images.get(s);
                    ImageView imageView = new ImageView(image);
                    ((Button) node).setGraphic(imageView);
                }
            }
            else {
                if (node instanceof Button && GridPane.getColumnIndex(node).equals(7-x) && GridPane.getRowIndex(node).equals(y)) {

                    ((Button) node).setGraphic(null);
                } else if (node instanceof Button && GridPane.getColumnIndex(node).equals(7-xnew) && GridPane.getRowIndex(node).equals(ynew)) {

                    //Ako bih sve uradio u jednoj liniji, program bi izbacivao "Invalid URL or resource not found" gresku
                    String s = this.board.getPiece(xnew, ynew).getImgFile() + ".png";
                    Image image = images.get(s);
                    ImageView imageView = new ImageView(image);
                    ((Button) node).setGraphic(imageView);
                }
            }
        }
    }

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

    public boolean isMoveSelected() {
        return moveSelected;
    }

    public void setMoveSelected(boolean moveSelected) {
        this.moveSelected = moveSelected;
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
}
