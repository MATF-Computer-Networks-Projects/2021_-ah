package chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessPiece{
    public Queen(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        int i = 0;

        for(; i<7; i++){
            if(y!=i) {
                list.add(new int[]{x, i});
            }
            if(x!=i) {
                list.add(new int[]{i, y});
            }
        }

        int dif = Math.abs(x-y);

        if(x<y)
            for (i = 0; i + dif < 8; i++) {
                if(i==x && i+dif == y)
                    continue;
                list.add(new int[]{i, i+dif});
            }
        else
            for (i = 0; i + dif < 8; i++) {
                if(i+dif==x && i == y)
                    continue;
                list.add(new int[]{i + dif, i});
            }

        //Inverzna dijagonala
        dif = Math.abs(7-y-x);

        if(x<7-y)
            for(i = 0; 7-i-dif >= 0; i++){
                if(7-i-dif==x && i == y)
                    continue;
                list.add(new int[]{7-i-dif, i});
            }
        else
            for(i = 0; i+dif < 8; i++){
                if(7-i==x && i+dif == y)
                    continue;
                list.add(new int[]{7-i, i+dif});
            }

        return list;
    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){
        int difx = xnew-x;
        int dify = ynew-y;

        //Ako treba da se krece dijagonalno
        if(Math.abs(difx) == Math.abs(dify)) {

            for (int i = 1; i < Math.abs(difx); i++)
                if (board.getPiece(x + Integer.signum(difx) * i, y + Integer.signum(dify) * i) != null)
                    return false;

            ChessPiece c = board.getPiece(xnew, ynew);

            if (c == null || c.getTeam() != this.team) {
                board.setPiece(this, xnew, ynew);
                board.setPiece(null, x, y);
                x = xnew;
                y = ynew;
                return true;
            }
        }

        //Ako treba da se krece vodoravno ili uspravno
        if(xnew != x && ynew == y){

            //da li postoje figure na putu do odabranog polja?
            for (int i=1; i<Math.abs(difx);i++){
                if(board.getPiece(x+Integer.signum(difx)*i, y) != null)
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
        } else if (ynew != y && xnew == x){
            for (int i=1; i<Math.abs(dify);i++){
                if(board.getPiece(x, y+Integer.signum(dify)*i) != null)
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
        return "queen";
    }
}
