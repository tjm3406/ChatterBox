package client;

import common.Receiver;

import java.io.IOException;
import java.net.Socket;

public class ClientReader extends Receiver {

    private ClientCommandInterface userInterface;
    private ClientWriter writer;


    public ClientReader(Socket client) throws IOException {
        super(client);
        userInterface = new ClientCommandInterface();
        writer = new ClientWriter(client);

    }

    @Override
    public void messageHandler(String message) {
        String[] messageArray = parse(message);
        switch (messageArray[0]){
            case "connect":
                connect(messageArray[1]);
                break;
            case "disconnect":
                disconnect();
                break;
            case "send_chat":
                sendChat(messageArray);
                break;
            case "send_whisper":
                sendWhisper(messageArray);
            case "list_users":
                listUsers();
                break;
        }

    }

    public void listUsers() {


    }

    public void sendWhisper(String[] messageArr) {

    }

    public void sendChat(String[] messageArr) {

    }

    public void disconnect() {

    }

    public void connect(String hostName) {

    }
}
