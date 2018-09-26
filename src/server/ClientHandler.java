/** Name: Tyler Miller
 *  Date: 3/27/18
 *  Assignment: Lab 8
 */
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
    private String username;
    private HashMap<String, ClientHandler> userDatabase;


    /**
     * Creates the servers client handler
     * @param client the socket between the client and server
     * @param userDatabase a collection of users, key is username and value is clienthandler object.
     */
    public ClientHandler(Socket client, HashMap<String, ClientHandler> userDatabase) throws IOException {
        super(client);
        input = new Scanner(client.getInputStream());
        output = new PrintStream(client.getOutputStream());
        this.userDatabase = userDatabase;

    }

    /**
     * Calls the proper methods to handle the various types of commands
     * @param message the message being handled
     */
    @Override
    public void messageHandler(String message) throws IOException {
            String[] messageArray = parse(message);
            switch (messageArray[0]){
                case CONNECT:
                    connect(messageArray[1]);
                    break;
                case DISCONNECT:
                    disconnect();
                    break;
                case SEND_CHAT:
                    send_chat(messageArray[1]);
                    break;
                case SEND_WHISPER:
                    send_whisper(messageArray[1]);
                    break;
                case LIST_USERS:
                    list_users();
                    break;
                default:
                    error("Default error");

            }
    }

    /**
     * Connects a user to the server
     * @param username the username to be connected
     */
    public void connect(String username) throws IOException {
        System.out.println("<<Unknown user: " + username);
        if(userDatabase.containsKey(username)) {
            fatal_error();
        }
        else {
            this.username = username;
            System.out.println(">>" + username + ": connected");

            this.connected();
            System.out.println(">>user_joined" + username);
            for(ClientHandler user : userDatabase.values()) {
                user.userJoined(username);
            }
            userDatabase.put(username, this);
            System.out.println(userDatabase);
        }
    }

    /**
     * Sends the connected message to the client
     */
    public void connected() throws IOException {
        output.write("connected\n".getBytes());
    }

    /**
     * Sends a user joined message to the client
     * @param username the user that joined
     */
    public void userJoined(String username) throws IOException {
        output.write(("user_joined::" + username + "\n").getBytes());
    }

    /**
     * Sends a user left message to the client
     * @param username the user that left
     */
    public void userLeft(String username) throws IOException {
        output.write(("user_left::" + username + "\n").getBytes());
    }

    /**
     * Disconnects this instance from the server
     */
    public void disconnect() throws IOException {
        System.out.println("<<" + username + ": disconnect");
        userDatabase.remove(username, this);

        output.write("disconnected\n".getBytes());
        System.out.println(">>" + username + ": disconnected");

        for(ClientHandler user : userDatabase.values()) {
            user.userLeft(username);
        }


    }

    /**
     * Sends a chatreceived message to this client
     * @param message the message needed to be sent
     */
    public void chatReceived(String message) throws IOException {
        System.out.println(">>" + username + " chat_received::" + message);
        output.write(("chat_received::" + message + "\n").getBytes());
    }

    /**
     * Sends a chat message to username
     * @param message the message to be sent
     */
    public void send_chat(String message) throws IOException {
        System.out.println("<<" + username + "send_chat::" + message);
        for(ClientHandler user : userDatabase.values()) {
            user.chatReceived(username + "::" + message);
        }
    }

    /**
     * Sets the client to receiving a whisper
     * @param message the message to be sent
     */
    public void whisperReceived(String message) throws IOException {
        System.out.println(">>" + username + " whisper_received::" + message);
        output.write(("whisper_received::" + message + "\n").getBytes());
    }

    /**
     * Sends a whisper to a client
     * @param message the message to be sent
     */
    public void send_whisper(String message) throws IOException {
        System.out.println("<<" + username + ": send_whisper::" + message);
        String[] messageArr = message.split("::", 2);
        String sentTo = messageArr[0];

        if(sentTo.contains(" "))
            error(" ");

        String messageSent = messageArr[1];

        userDatabase.get(messageArr[0]).whisperReceived(username + "::" + messageSent);

        this.whisperSent(message);
    }

    /**
     * Shows the client that the whisper was sent
     * @param message the message to be sent
     */
    public void whisperSent(String message) throws IOException {
        System.out.println(">>" + username + ": whisper_sent::" + message);
        output.write(("whisper_sent::" + message + "\n").getBytes());
    }

    /**
     * The command the client sends to the server to get the list of users
     */
    public void list_users() throws IOException {
        System.out.println("<<" + username + "list_users");
        output.write("users::".getBytes());
        for(String name : userDatabase.keySet()) {
            output.write(("::" + name).getBytes());
        }
        output.write("\n".getBytes());
    }

    /**
     * An error that forces a disconnect
     */
    public void fatal_error() throws IOException {
        this.disconnect();
    }

    /**
     * An error that doesn't force a disconnect
     * @param error the error code
     */
    public void error(String error) throws IOException {
        if(error.equals(" "))
            output.write("Can't whisper to people with spaces in username. RIP.\n".getBytes());
        else
            output.write("An error occurred. Please try again.\n".getBytes());
    }

}
