package common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Receiver implements ChatterboxProtocol, Runnable  {

    private InputStream input;
    private OutputStream output;

    public Receiver(Socket client){
        try {
            InputStream input = client.getInputStream();
            this.input = input;
            OutputStream output = client.getOutputStream();
            this.output = output;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {



    }

    public abstract void messageHandler();

}
