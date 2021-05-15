package chess;

import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.restartPositions();
        System.out.println(board);



        while(true){
            int x;
            int y;
            int xnew;
            int ynew;


            System.out.println("Unesite koord. trazene figure");
            Scanner sc = new Scanner(System.in);

            x = sc.nextInt();
            if(x==-1)
                break;

            y = sc.nextInt();

            while(board.getPiece(x, y)==null || x<0 || y<0 || x>7 || y>7){
                System.out.println("Greska, unesite nove koordinate");
                x = sc.nextInt();
                y = sc.nextInt();
            }

            System.out.println("Odabrana figura: " + board.getPiece(x, y).getType()+", tim: "+board.getPiece(x, y).getTeam());

            System.out.println("Unesite koord. polja gde pomerate figuru");

            xnew = sc.nextInt();
            ynew = sc.nextInt();

            if(xnew<0 || ynew<0 || xnew>7 || ynew>7){
                System.out.println("Nepostojece koordinate");
                System.out.println(board);
            } else if(!board.getPiece(x, y).move(board, xnew, ynew)){
                System.out.println("Nemoguc potez");
                System.out.println(board);
            } else {
                System.out.println(board);
            }
        }
    }
}
