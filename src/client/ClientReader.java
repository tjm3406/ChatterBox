package client;

import common.Receiver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientReader extends Receiver {

    private Scanner input;
    private PrintStream output;


    public ClientReader(Socket client) throws IOException {
        super(client);
        this.input = new Scanner(client.getInputStream());
        this.output = new PrintStream(client.getOutputStream());

    }

    @Override
    public void messageHandler(String message) {
        String[] messageArray = parse(message);
        switch (messageArray[0]){
            case CONNECTED:
                connected();
                break;
            case DISCONNECTED:
                disconnected();
                break;
            case CHAT_RECEIVED:
                chatReceived(messageArray);
                break;
            case WHISPER_RECEIVED:
                whisperReceived(messageArray);
            case WHISPER_SENT:
                whisperSent(messageArray);
                break;
            case USERS:
                users(messageArray);
                break;
            case USER_JOINED:
                userJoined(messageArray[1]);
                break;
            case USER_LEFT:
                userLeft(messageArray[1]);
                break;
            case ERROR:
                error(messageArray[1]);
                break;
            case FATAL_ERROR:
                fatalError(messageArray[1]);
                break;


        }
    }

    public void fatalError(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.out.println(message);
    }

    public void userLeft(String username) {
        System.out.println("A user has left the server: " + username);
    }

    public void userJoined(String username) {
        System.out.println("A user has joined the chatterbox server: " + username);
    }

    public void users(String[] messageArr) {
        String[] users = messageArr[1].split("::");
        for(int i = 0; i < users.length; i++) {
            System.out.println(users[i]);
        }
    }

    public void whisperSent(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println("You whispered to " + splitSecond[0] + ": " + splitSecond[1]);
    }

    public void whisperReceived(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println(splitSecond[0] + " whispers to you: " + splitSecond[1]);
    }

    public void chatReceived(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println(splitSecond[0] + " said: " + splitSecond[1]);
    }

    public void disconnected() {
        System.out.println(DISCONNECTED);
    }

    public void connected() {
        System.out.println(CONNECTED);
    }

    public void listUsers() {
        try {
            output.write("list_users\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWhisper(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendChat(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            output.write("disconnect\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(String hostName) {
        try {
            output.write("connect::".getBytes());
            output.write(hostName.getBytes());
            output.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
