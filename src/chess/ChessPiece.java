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


    public Team getTeam() {
        return team;
    }
}
