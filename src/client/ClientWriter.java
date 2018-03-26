package client;

public class ClientWriter {

    public void messageHandler(String message) {
        String[] messageArray = parse(message);
        switch (messageArray[0]){
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
            case LIST_USERS:
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
        output.println(DISCONNECTED);
    }

    public void connect(String hostName) {
        output.println(CONNECT);
    }
}
