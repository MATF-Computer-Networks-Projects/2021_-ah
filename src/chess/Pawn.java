package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece{
    public Pawn(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        if(y<7) {
            list.add(new int[]{x, y + 1});
            if(x>0) {
                list.add(new int[]{x - 1, y + 1});
            }
            if(x<7) {
                list.add(new int[]{x + 1, y + 1});
            }
        }
        return list;
    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){

        switch(Math.abs(ynew - y)){
            case 2:
                if(!((y==6 && team == Team.BLACK) || (y==1 && team == Team.WHITE)))
                    return false;
                break;
            case 1:
                break;
            default:
                return false;
        }

        //Pion ne sme da se krece unazad
        switch(team){
            case BLACK:
                if(ynew > y)
                    return false;
            case WHITE:
                if(ynew < y)
                    return false;
        }

        ChessPiece c = board.getPiece(xnew, ynew);

        switch(Math.abs(xnew-x)) {
            //Pion moze da se pomeri dijagonalno samo ako se na tom polju nalazi figura iz suprotnog tima
            case 1:
                if(c != null && c.getTeam() != this.team){
                    board.setPiece(this, xnew, ynew);
                    board.setPiece(null, x, y);
                    x = xnew;
                    y = ynew;
                    return true;
                }
                return false;
            //Pomera se napred
            case 0:
                if(c == null){
                    board.setPiece(this, xnew, ynew);
                    board.setPiece(null, x, y);
                    x = xnew;
                    y = ynew;
                    return true;
                }
                return false;

            default: return false;
        }
    }

    public String getType(){
        return "Pawn";
    }
}
