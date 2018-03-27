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

    private ClientCommandInterface userInterface;
    private Scanner input;
    private PrintStream output;


    public ClientReader(Socket client) throws IOException {
        super(client);
        userInterface = new ClientCommandInterface();
        this.input = new Scanner(client.getInputStream());
        this.output = new PrintStream(client.getOutputStream());

    }

    @Override
    public void messageHandler(String message) {
        String[] messageArray = parse(message);
        switch (messageArray[0]){
            case CONNECTED:
                connected(messageArray[1]);
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
            case CONNECT:
                connect(messageArray[1]);
                break;
            case DISCONNECT:
                disconnect();
                break;
            case SEND_CHAT:
                sendChat(messageArray);
                break;
            case SEND_WHISPER:
                sendWhisper(messageArray);
                break;
            case LIST_USERS:
                listUsers();
                break;

        }
    }

    public void fatalError(String message) {
        output.println(message);
    }

    public void error(String message) {
        output.println(message);
    }

    public void userLeft(String username) {
        output.println("A user has left the server: " + username);
    }

    public void userJoined(String username) {
        output.println("A user has joined the chatterbox server: " + username);
    }

    public void users(String[] messageArr) {
        for(int i = 0; i < messageArr.length; i++) {
            if(i >= 1) {
                output.println(messageArr[i]);
            }
        }
    }

    public void whisperSent(String[] messageArr) {
        output.println("You whispered to " + messageArr[1] + ": " + messageArr[2]);
    }

    public void whisperReceived(String[] messageArr) {
        output.println(messageArr[1] + " whispers to you: " + messageArr[2]);
    }

    public void chatReceived(String[] messageArr) {
        output.println(messageArr[1] + " said: " + messageArr[2]);
    }

    public void disconnected() {
        output.println(DISCONNECTED);
    }

    public void connected(String hostName) {
        output.println(CONNECT);
    }

    public void listUsers() {
        try {
            output.write("list_users\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWhisper(String[] messageArr) {
        try {
            output.write("send_whisper::".getBytes());
            output.write(messageArr[0].getBytes());
            output.write("::".getBytes());
            output.write(messageArr[1].getBytes());
            output.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendChat(String message) {
        try {
            output.write("send_chat::".getBytes());
            output.write(message.getBytes());
            output.write("\n".getBytes());
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
