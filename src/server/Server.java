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

    private HashMap<String, ClientReader> userDatabase;
    private Scanner input;
    private PrintStream output;
    private static ServerSocket server;

    public static void main(String[] args) throws IOException {
        while (true) {
            Socket client = server.accept();
            ClientHandler handler = new ClientHandler(client);
            new Thread(handler).start();
        }

    }
}