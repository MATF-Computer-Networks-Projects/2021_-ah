package chess;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;


public class Bishop extends ChessPiece{
    public Bishop(int x, int y, Team team) {
        super(x, y, team);
    }

    @Override
    public List<int[]> possibleMoves() {
        List<int[]> list = new ArrayList<>();

        //Regularna dijagonala
        int dif = Math.abs(x-y);

        if(x<y)
            for (int i = 0; i + dif < 8; i++) {
                if(i==x && i+dif == y)
                    continue;
                list.add(new int[]{i, i+dif});
            }
        else
            for (int i = 0; i + dif < 8; i++) {
                if(i+dif==x && i == y)
                    continue;
                list.add(new int[]{i + dif, i});
            }

        //Inverzna dijagonala
        dif = Math.abs(7-y-x);
        if(x<7-y)
            for(int i = 0; 7-i-dif >= 0; i++){
                if(7-i-dif==x && i == y)
                    continue;
                list.add(new int[]{7-i-dif, i});
            }
        else
            for(int i = 0; i+dif < 8; i++){
                if(7-i==x && i+dif == y)
                    continue;
                list.add(new int[]{7-i, i+dif});
            }

        return list;

//Losija verzija. Sacuvana za slucaj da bolja verzija ne funkcionise
//        int i = x;
//        int j = y;
//        while(i<7 && j<7){
//            i++;
//            j++;
//            list.add(new int[]{i, j});
//        }
//
//        i = x;
//        j = y;
//        while(i<7 && j>0){
//            i++;
//            j--;
//            list.add(new int[]{i, j});
//        }
//
//        i = x;
//        j = y;
//        while(i>0 && j<7){
//            i--;
//            j++;
//            list.add(new int[]{i, j});
//        }
//
//        i = x;
//        j = y;
//        while(i>0 && j>0){
//            i--;
//            j--;
//            list.add(new int[]{i, j});
//        }

    }

    @Override
    public boolean move(ChessBoard board, int xnew, int ynew){

        int difx = xnew-x;
        int dify = ynew-y;

        //Da li se novo polje nalazi na nekoj od dijagonala iz odabranog polja
        if(Math.abs(difx) != Math.abs(dify))
            return false;

        for(int i=1; i<Math.abs(difx); i++)
            if(board.getPiece(x+Integer.signum(difx)*i, y+Integer.signum(dify)*i) != null)
                return false;

        ChessPiece c = board.getPiece(xnew, ynew);

        if (c == null || c.getTeam() != this.team){
            board.setPiece(null, x, y);
            x = xnew;
            y = ynew;
            board.setPiece(this, xnew, ynew);
            return true;
        }



        return false;
    }

    public String getType(){
        return "bishop";
    }
}
