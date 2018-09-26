/** Name: Tyler Miller
 *  Date: 3/27/18
 *  Assignment: Lab 8
 */
package server;

import client.ClientReader;
import common.Receiver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server {


    /**
     * Runs a multi-threaded server loop
     */
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(6789);
        HashMap<String, ClientHandler> userDatabase = new HashMap<>();

        while (true) {
            System.out.println("Waiting for connections on port " + server.getLocalPort());
            Socket client = server.accept();
            System.out.println("Client connection received from " + client.getInetAddress());
            ClientHandler handler = new ClientHandler(client, userDatabase);
            new Thread(handler).start();

        }

    }
}