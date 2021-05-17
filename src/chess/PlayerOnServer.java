package chess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerOnServer {
    private Socket socket;
    private DataInputStream istream;
    private DataOutputStream ostream;

    public PlayerOnServer(Socket socket) {
        this.socket = socket;
        try {
            istream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ostream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getIstream() {
        return istream;
    }

    public DataOutputStream getOstream() {
        return ostream;
    }
}
