package chess;

import java.util.ArrayList;
import java.util.List;

public abstract class ChessPiece {
    protected Team team;
    protected int x;
    protected int y;

    public ChessPiece(int x, int y, Team team) {
        this.team = team;
        this.x = x;
        this.y = y;
    }

    //Vraca sva polja gde odabrana figura moze biti pomerena
    public abstract List<int[]> possibleMoves();

    //Pomeranje figure
    public abstract boolean move(ChessBoard board, int xnew, int ynew);

    public abstract String getType();

    public Team getTeam() {
        return team;
    }

    //Vraca ime png fajla za ucitavanje slika u glavnoj aplikaciji
    public String getImgFile(){
        String teamChar;
        if(team == Team.WHITE)
            teamChar = "w";
        else
            teamChar = "b";
        return this.getType()+teamChar;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
