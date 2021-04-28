package chess;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private ChessPiece[][] pieces = new ChessPiece[8][8];

    public ChessPiece getPiece(int x, int y){
        return pieces[x][y];
    }





    //Provera da li je potez iz move() metode dozvoljen
    public void isLegalMove(int x, int y, int xnew, int ynew){


    }

    //Pomeranje figure
    public void move(int x, int y, int xnew, int ynew){


    }


    //Restartuje pozicije svih figura pred početak nove partije
    public void restartPositions(){
        pieces[0][0] = new Rook(0, 0, Team.WHITE);
        pieces[1][0] = new Knight(1, 0, Team.WHITE);
        pieces[2][0] = new Bishop(2, 0, Team.WHITE);
        pieces[3][0] = new Queen(3, 0, Team.WHITE);
        pieces[4][0] = new King(4, 0, Team.WHITE);
        pieces[5][0] = new Bishop(5, 0, Team.WHITE);
        pieces[6][0] = new Knight(6, 0, Team.WHITE);
        pieces[7][0] = new Rook(7, 0, Team.WHITE);
        pieces[0][7] = new Rook(0, 7, Team.BLACK);
        pieces[1][7] = new Knight(1, 7, Team.BLACK);
        pieces[2][7] = new Bishop(2, 7, Team.BLACK);
        pieces[3][7] = new Queen(3, 7, Team.BLACK);
        pieces[4][7] = new King(4, 7, Team.BLACK);
        pieces[5][7] = new Bishop(5, 7, Team.BLACK);
        pieces[6][7] = new Knight(6, 7, Team.BLACK);
        pieces[7][7] = new Rook(7, 7, Team.BLACK);

        for(int i=0; i<8; i++){
            pieces[i][6] = new Pawn(i, 6, Team.BLACK);
            pieces[i][1] = new Pawn(i, 1, Team.WHITE);
            for(int j=2; j<6; j++){
                pieces[i][j] = null;
            }
        }

    }
}