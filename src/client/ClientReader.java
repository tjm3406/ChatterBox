package client;

import common.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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
                chatRecieved(messageArray);
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
        output.println(message);
    }

    public void error(String message) {
        output.println(message);
    }

    public void userLeft(String username) {

    }

    public void userJoined(String username) {

    }

    public void users(String[] messageArr) {

    }

    public void whisperSent(String[] messageArr) {

    }

    public void whisperReceived(String[] messageArr) {

    }

    public void chatRecieved(String[] messageArr) {

    }

    public void disconnected() {
        output.println(DISCONNECTED);
    }

    public void connected(String hostName) {
        output.println(CONNECT);
    }
}
