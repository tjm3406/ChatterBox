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
    private OutputStream output;
    private Socket client;
    private String message;
    private String host;
    private String target;

    public Receiver(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        OutputStream output = client.getOutputStream();
        this.input = input;
        this.output = output;
        this.client = client;

    }

    public String[] parse(String message) {
        String[] messageArr = message.split("::");
        this.message = messageArr[0];
        host = messageArr[1];
        target = messageArr[2];
        return messageArr;
    }

    public void run() {
        Scanner s = new Scanner(input);
        PrintStream printer = new PrintStream(output);
        while(s.hasNext()) {
            String message = s.nextLine();
            parse(message);
            if(message.equals("quit"))
                break;
            printer.println();
        }
        try {
            client.shutdownInput();
            client.shutdownOutput();
        } catch (IOException e) {

        }


    }

    public abstract void messageHandler(String message);

}
