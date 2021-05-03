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

    }
}
