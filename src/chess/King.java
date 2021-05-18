package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    public King(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        for (int i = x - 1; i < x + 1; i++) {
            if (i < 0 || i > 7) continue;
            for (int j = y - 1; j < y + 1; j++) {
                if (j < 0 || j > 7) continue;
                list.add(new int[]{i, j});
            }
        }

        return list;
    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){

        if(Math.abs(ynew - y) != 1 && Math.abs(xnew - x) != 1)
            return false;

        ChessPiece c = board.getPiece(xnew, ynew);
        if(c == null || c.getTeam() != this.team){
            board.setPiece(null, x, y);
            x = xnew;
            y = ynew;
            board.setPiece(this, xnew, ynew);
            return true;
        }
        return false;
    }

    public String getType(){
        return "king";
    }
}