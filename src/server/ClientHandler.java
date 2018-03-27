package server;

import common.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Receiver {

    private Scanner input;
    private PrintStream output;
    private Socket client;
    private HashMap<String, ClientHandler> userDatabase;
    private String username;


    public ClientHandler(Socket client) throws IOException {
        super(client);
        input = new Scanner(client.getInputStream());
        output = new PrintStream(client.getOutputStream());
        userDatabase = new HashMap<>();

    }

    @Override
    public void messageHandler(String message) {
            String[] messageArray = parse(message);
            switch (messageArray[0]){
                case CONNECT:
                    connect(messageArray[1]);
                case DISCONNECT:
                    disconnect();
                case SEND_CHAT:
                    send_chat(messageArray);
                case SEND_WHISPER:
                    send_whisper(messageArray);
                case LIST_USERS:
                    list_users();

            }
    }

    public void connect(String username) {
        if(userDatabase.containsKey(username)) {
            fatal_error();
        }
        else {
            System.out.println("Unknown user: " + username);
            this.username = username;
            userDatabase.put(username, this);
        }
    }

    public void disconnect() {

    }

    public void send_chat(String[] message) {

    }

    public void send_whisper(String[] message) {

    }

    public void list_users() {

    }

    public void fatal_error() {

    }

}
