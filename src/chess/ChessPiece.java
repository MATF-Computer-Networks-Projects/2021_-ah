package chess;

import java.util.ArrayList;
import java.util.List;

public abstract class ChessPiece {
    Team team;
    int x;
    int y;

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
}
