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
    public void messageHandler(String message) throws IOException {
            String[] messageArray = parse(message);
            switch (messageArray[0]){
                case CONNECT:
                    connect(messageArray[1]);
                case DISCONNECT:
                    disconnect();
                case SEND_CHAT:
                    send_chat(messageArray[1]);
                case SEND_WHISPER:
                    send_whisper(messageArray[1]);
                case LIST_USERS:
                    list_users();

            }
    }

    public void connect(String username) throws IOException {
        System.out.println("<<Unknown user: " + username);
        if(userDatabase.containsKey(username)) {
            fatal_error();
        }
        else {
            this.username = username;
            userDatabase.put(username, this);
            System.out.println(">>" + username + ": connected");

            this.connected();
            System.out.println(">>user_joined" + username);
            for(ClientHandler user : userDatabase.values()) {
                if(!user.equals(this))
                    user.userJoined(username);
            }
        }
    }

    public void connected() throws IOException {
        output.write("connected\n".getBytes());
    }

    public void userJoined(String username) throws IOException {
        output.write(("user_joined::" + username + "\n").getBytes());
    }

    public void userLeft(String username) throws IOException {
        output.write(("user_left::" + username + "\n").getBytes());
    }

    public void disconnect() throws IOException {
        System.out.println("<<" + username + ": disconnect");
        userDatabase.remove(username, this);

        output.write("disconnected\n".getBytes());
        System.out.println(">>" + username + ": disconnected");

        for(ClientHandler user : userDatabase.values()) {
            user.userLeft(username);
        }

    }

    public void chatReceived(String message) throws IOException {
        System.out.println(">>" + username + " chat_received::" + message);
        output.write(("chat_received::" + message + "\n").getBytes());
    }

    public void send_chat(String message) throws IOException {
        System.out.println("<<" + username + "send_chat::" + message);
        for(ClientHandler user : userDatabase.values()) {
            user.chatReceived(username + "::" + message);
        }
    }

    public void whisperReceived(String message) throws IOException {
        System.out.println(">>" + username + " whisper_received::" + message);
        output.write(("whisper_received::" + message + "\n").getBytes());
    }

    public void send_whisper(String message) throws IOException {
        System.out.println("<<" + username + ": send_whisper::" + message);
        String[] messageArr = message.split("::", 2);
        String sentTo = messageArr[0];
        String messageSent = messageArr[1];

        userDatabase.get(messageArr[0]).whisperReceived(username + "::" + messageSent);

        this.whisperSent(message);
    }

    public void whisperSent(String message) throws IOException {
        System.out.println(">>" + username + ": whisper_sent::" + message);
        output.write(("whisper_sent::" + message + "\n").getBytes());
    }

    public void list_users() throws IOException {
        System.out.println("<<" + username + "list_users");
        output.write("users::".getBytes());
        for(String name : userDatabase.keySet()) {
            output.write(("::" + name).getBytes());
        }
        output.write("\n".getBytes());

    }

    public void fatal_error() {

    }

}
