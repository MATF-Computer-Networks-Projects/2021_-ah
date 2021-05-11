package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece{

    public Rook(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        for(int i = 0; i<7; i++){
            if(y!=i) {
                list.add(new int[]{x, i});
            }
            if(x!=i) {
                list.add(new int[]{i, y});
            }
        }
        return list;
    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){

        if(xnew!=x && ynew != y)
            return false;

        //Pomeranje po x osi
        if(xnew != x){
           int dif = xnew-x;

           //da li postoje figure na putu do odabranog polja?
           for (int i=1; i<Math.abs(dif);i++){
               if(board.getPiece(x+Integer.signum(dif)*i, y) != null)
                   return false;
           }

           //da li postoji figura na odabranu polju? ako postoji, proverava da li pripada suprotnom timu i "jede" je
           ChessPiece c = board.getPiece(xnew, ynew);

           if(c == null || c.getTeam() != this.team){
                board.setPiece(this, xnew, ynew);
                board.setPiece(null, x, y);
                x = xnew;
                y = ynew;
                return true;
           }
        //Pomeranje po y osi
        } else if (ynew != y){
            int dif = ynew-y;
            for (int i=1; i<Math.abs(dif);i++){
                if(board.getPiece(x, y+Integer.signum(dif)*i) != null)
                    return false;
            }

            ChessPiece c = board.getPiece(xnew, ynew);

            if(c == null || c.getTeam() != this.team){
                board.setPiece(this, xnew, ynew);
                board.setPiece(null, x, y);
                x = xnew;
                y = ynew;
                return true;
            }
        }
        return false;
    }


    public String getType(){
        return "rook";
    }
}
