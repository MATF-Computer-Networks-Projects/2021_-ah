package chess;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ChessClient {
    private String hostname;
    private int port;
    private Team team;

    public ChessClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
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

    public int getPort() {
        return port;
    }
}
