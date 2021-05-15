package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece{
    public Knight(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        if(x<6){
            if(y<7) {
                list.add(new int[]{x + 2, y + 1});
            }
            if(y>0) {
                list.add(new int[]{x + 2, y - 1});
            }
        }

        if(x>1){
            if(y<7) {
                list.add(new int[]{x - 2, y + 1});
            }
            if(y>0) {
                list.add(new int[]{x - 2, y - 1});
            }
        }

        if(y<6){
            if(x<7) {
                list.add(new int[]{x + 1, y + 2});
            }
            if(x>0) {
                list.add(new int[]{x - 1, y + 2});
            }
        }

        if(y>1){
            if(x<7) {
                list.add(new int[]{x + 1, y - 2});
            }
            if(x>0) {
                list.add(new int[]{x - 1, y - 2});
            }
        }

        return list;
    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){
        ChessPiece c = board.getPiece(xnew, ynew);

        if(Math.abs(xnew-x)*Math.abs(ynew-y)!=2){
            return false;
        }

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
        return "knight";
    }
}
