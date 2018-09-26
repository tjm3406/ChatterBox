/** Name: Tyler Miller
 *  Date: 3/27/18
 *  Assignment: Lab 8
 */
package common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public abstract class Receiver implements ChatterboxProtocol, Runnable  {

    private InputStream input;
    private Socket client;

    /**
     * The constructor for Receiver
     * @param client the socket the client uses to communicate
     */
    public Receiver(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        this.input = input;
        this.client = client;
    }

    /**
     * parses the message by the separator
     * @param message the message being sent
     * @return a string array of the parsed message
     */
    public String[] parse(String message) {
        String[] messageArr = message.split("::", 2);
        return messageArr;
    }

    /**
     * Starts a client or clientHandler thread
     */
    public void run() {
        Scanner s = new Scanner(input);
        while(s.hasNext()) {
            String message = s.nextLine();

            try {
                messageHandler(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(message.equals(DISCONNECT) || message.equals(DISCONNECTED)) {
                break;
            }
        }
        try {
            client.shutdownInput();
            client.shutdownOutput();
        } catch (IOException e) {

        }


    }

    /**
     * Handles the various messages needed. Abstract because the implementation of the messages are different
     * @param message the message being handles
     */
    public abstract void messageHandler(String message) throws IOException;

}
