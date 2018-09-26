/** Name: Tyler Miller
 *  Date: 3/27/18
 *  Assignment: Lab 8
 */
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


    /**
     * Creates a client side reader
     * @param client the socket used to crete a client
     */
    public ClientReader(Socket client) throws IOException {
        super(client);
        this.input = new Scanner(client.getInputStream());
        this.output = new PrintStream(client.getOutputStream());

    }

    /**
     * Handles the messages for the client side
     * @param message the message being handled
     */
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
                break;
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

    /**
     * Sends an error that forces disconnect
     * @param message the message
     */
    public void fatalError(String message) {
        System.out.println(message);
    }

    /**
     * An error that doesn't force disconnect
     * @param message the message
     */
    public void error(String message) {
        System.out.println(message);
    }

    /**
     * Prints to the client that a user left
     * @param username the user that left
     */
    public void userLeft(String username) {
        System.out.println("A user has left the server: " + username);
    }

    /**
     * Prints to the client that the user joined
     * @param username the user that joined
     */
    public void userJoined(String username) {
        System.out.println("A user has joined the chatterbox server: " + username);
    }

    /**
     * Prints the list of users from the command sent by the server
     * @param messageArr an array of users
     */
    public void users(String[] messageArr) {
        String[] users = messageArr[1].split("::");
        for(int i = 0; i < users.length; i++) {
            System.out.println(users[i]);
        }
    }

    /**
     * Prints that a whisper was sent to someone
     * @param messageArr an array that contains who the whisper was sent to and the message sent
     */
    public void whisperSent(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println("You whispered to " + splitSecond[0] + ": " + splitSecond[1]);
    }

    /**
     * Prints that a whisper was received by this client
     * @param messageArr an array that contains who sent the whisper to and the message sent
     */
    public void whisperReceived(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println(splitSecond[0] + " whispers to you: " + splitSecond[1]);
    }

    /**
     * Prints that a chat was received
     * @param messageArr An array containing the message and who said it
     */
    public void chatReceived(String[] messageArr) {
        String[] splitSecond = messageArr[1].split("::", 2);
        System.out.println(splitSecond[0] + " said: " + splitSecond[1]);
    }

    /**
     * Prints that you were disconnected
     */
    public void disconnected() {
        System.out.println(DISCONNECTED);
    }

    /**
     * Prints that there was a successful connection
     */
    public void connected() {
        System.out.println(CONNECTED);
        System.out.println("Welcome to the chatterbox application! Type '/help' to see a list of commands.");
    }

    /**
     * Writes the list command to the server
     */
    public void listUsers() {
        try {
            output.write("list_users\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a send whisper command to the server
     * @param message the message to be written in the :: line terminating form
     */
    public void sendWhisper(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a send chat command to the server
     * @param message the message to be written in the :: line terminating form
     */
    public void sendChat(String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a disconnect attempt to the server
     */
    public void disconnect() {
        try {
            output.write("disconnect\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a connect attempt to the server
     * @param hostName the username that is attempting to connect
     */
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
