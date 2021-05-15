package chess;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class TestMainUI extends Application {

    boolean buttonSelected = false;

    int x;
    int y;
    int xnew;
    int ynew;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws FileNotFoundException {

        ChessBoard board = new ChessBoard();
        board.restartPositions();

        GridPane pane = new GridPane();

        pane.setGridLinesVisible(true);


        pane.setPadding(new Insets(50, 50, 50, 50));
        pane.setVgap(2);
        pane.setHgap(2);

        initialize(board, pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);

        stage.show();
    }


    private void initialize(ChessBoard board, GridPane pane) throws FileNotFoundException {
        boolean grey = true;
        for(int i=0;i<8;i++) {
            for (int j = 0; j < 8; j++) {

                //Odabrano polje nema figuru
                if (board.getPiece(j, i) == null) {
                    Button button = new Button("");
                    button.setPrefSize(60, 60);
                    button.setPadding(new Insets(0, 0, 0, 0));


                    if (grey)
                        button.setStyle("-fx-background-color: Grey");
                    else
                        button.setStyle("-fx-background-color: White");

                    pane.add(button, j, 7-i);



                    button.setOnMouseClicked( e -> {

                        //Biranje prvog polja u potezu
                        if(!buttonSelected){
                            x = GridPane.getColumnIndex(button);
                            y = 7-GridPane.getRowIndex(button);
                            if(board.getPiece(x, y)==null){
                                System.out.println("no piece on coords: "+x+" "+y);
                            }else{
                                System.out.println("piece: "+board.getPiece(x, y).getType()+" coords: "+x+" "+y);
                                buttonSelected = !buttonSelected;
                            }
                        }
                        //Biranje drugog polja i pomeranje ako je dozvoljeno
                        else{
                            xnew = GridPane.getColumnIndex(button);
                            ynew = 7-GridPane.getRowIndex(button);

                            //Ako je potez legalan
                            if(board.getPiece(x, y).move(board, xnew, ynew)){

                                //Prebacuje sliku figure na nove koordinate
                                try {
                                    Image newImage = new Image(new FileInputStream("M:\\Projects\\2021_Sah\\icons\\" + board.getPiece(xnew, ynew).getImgFile() + ".png"));
                                    button.setGraphic(new ImageView(newImage));
                                } catch (FileNotFoundException fileNotFoundException) {
                                    fileNotFoundException.printStackTrace();
                                }

                                //Uklanja sliku sa starih koordinata
                                getNodeByCoordinate(x, y, pane).setGraphic(null);

                            }else{
                                System.out.println("illegal move!");
                            }
                            buttonSelected = !buttonSelected;
                        }
                    });

                //Odabrano polje ima figuru
                } else {
                    //Stilizovanje
                    Image image = new Image(new FileInputStream("M:\\Projects\\2021_Sah\\icons\\" + board.getPiece(j, i).getImgFile() + ".png"));
                    Button button = new Button("", new ImageView(image));
                    button.setPadding(new Insets(0, 0, 0, 0));
                    button.setPrefSize(60, 60);


                    if (grey)
                        button.setStyle("-fx-background-color: Grey");
                    else
                        button.setStyle("-fx-background-color: White");

                    pane.add(button, j, 7-i);



                    button.setOnMouseClicked( e -> {

                        //Biranje prvog polja u potezu
                        if(!buttonSelected){
                            x = GridPane.getColumnIndex(button);
                            y = 7-GridPane.getRowIndex(button);
                            if(board.getPiece(x, y)==null){
                                System.out.println("no piece on coords: "+x+" "+y);
                            }else{
                                System.out.println("piece: "+board.getPiece(x, y).getType()+" coords: "+x+" "+y);
                                buttonSelected = !buttonSelected;
                            }
                        }

                        //Biranje drugog polja i pomeranje ako je dozvoljeno
                        else{
                            xnew = GridPane.getColumnIndex(button);
                            ynew = 7-GridPane.getRowIndex(button);

                            //Ako je potez legalan
                            if(board.getPiece(x, y).move(board, xnew, ynew)){

                                //Prebacuje sliku figure na nove koordinate
                                try {
                                    Image newImage = new Image(new FileInputStream("M:\\Projects\\2021_Sah\\icons\\" + board.getPiece(xnew, ynew).getImgFile() + ".png"));
                                    button.setGraphic(new ImageView(newImage));
                                } catch (FileNotFoundException fileNotFoundException) {
                                    fileNotFoundException.printStackTrace();
                                }

                                //Uklanja sliku sa starih koordinata
                                getNodeByCoordinate(x, y, pane).setGraphic(null);

                            }else{
                                System.out.println("illegal move!");
                            }

                            buttonSelected = !buttonSelected;
                        }
                    });


                }
                grey = !grey;
            }
            grey = !grey;
        }
    }

    Button getNodeByCoordinate(Integer x, Integer y, GridPane pane) {


        for (Node node : pane.getChildren()) {
            if(node instanceof Button && GridPane.getColumnIndex(node).equals(x) && GridPane.getRowIndex(node).equals(7-y)){
                return (Button)node;
            }
        }
        return null;
    }
}
