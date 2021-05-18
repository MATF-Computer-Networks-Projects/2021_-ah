package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatUserThread extends Thread{
    private Socket socket;
    private ChessChatServer chatServer;
    private PrintWriter messageSender;
    private int index;

    ChatUserThread(Socket socket, ChessChatServer server, int index) {
        this.socket = socket;
        this.chatServer = server;
        this.index = index;
        try {
            this.messageSender = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader userMessageReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));


            String userMessage;
            do {
                userMessage = userMessageReader.readLine();
                if (userMessage == null)
                    break;
                this.chatServer.broadcast(userMessage, index);
            } while (!userMessage.equals("cao"));



            this.socket.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message){
        messageSender.println(message);
    }
}
