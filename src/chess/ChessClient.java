package chess;



public class ChessClient {
    private String hostname;
    private Team team;

    public ChessClient(String hostname) {
        this.hostname = hostname;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getHostname() {
        return hostname;
    }

}
