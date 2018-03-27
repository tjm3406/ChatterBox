package server;

import client.ClientReader;
import common.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server extends Receiver {

    private HashMap<String, ClientReader> userDatabase;
    private Scanner input;
    private PrintStream output;

    public Server(Socket client) throws IOException {
        super(client);
        userDatabase = new HashMap<>();
        this.input = new Scanner(client.getInputStream());
        this.output = new PrintStream(client.getOutputStream());
    }

    @Override
    public void messageHandler(String message) {
        String[] messageArray = parse(message);
        switch (messageArray[0]){
            case CONNECT:

        }
    }

    public void connect(String username) {


    }

    public void disconnect() {

    }

    public void send_chat(String message) {

    }

    public void send_whisper(String message) {

    }

    public void list_users() {

    }
}
