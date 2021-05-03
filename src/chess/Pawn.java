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
        if(ynew - y != 1)
            return false;

        ChessPiece c = board.getPiece(xnew, ynew);

        switch(Math.abs(xnew-x)) {
            //Pion moze da se pomeri dijagonalno samo ako se na tom polju nalazi figura iz suprotnog tima
            case 1:
                if(c != null && c.getTeam() != this.team){
                    board.setPiece(this, xnew, ynew);
                    board.setPiece(null, x, y);
                    return true;
                }
                else return false;
            case 0:
                c = board.getPiece(xnew, ynew);
                if(c == null){
                    board.setPiece(this, xnew, ynew);
                    board.setPiece(null, x, y);
                    return true;
                }
                else return false;

            default: return false;
        }
    }
}
