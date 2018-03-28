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



    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(6789);

        while (true) {
            Socket client = server.accept();
            ClientHandler handler = new ClientHandler(client);
            new Thread(handler).start();
        }

    }
}