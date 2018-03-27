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

    public Receiver(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        this.input = input;
        this.client = client;

    }

    public String[] parse(String message) {
        String[] messageArr = message.split("::", 2);
        return messageArr;
    }

    public void run() {
        Scanner s = new Scanner(input);
        while(s.hasNext()) {
            String message = s.nextLine();
            messageHandler(message);
        }
        try {
            client.shutdownInput();
            client.shutdownOutput();
        } catch (IOException e) {

        }


    }

    public abstract void messageHandler(String message);

}
