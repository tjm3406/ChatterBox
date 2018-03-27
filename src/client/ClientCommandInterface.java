package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommandInterface {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a host: ");
        String ipAddress = input.nextLine();
        int port = 6789;

        Socket client = new Socket(ipAddress, port);

        System.out.println("Enter a username: ");
        String userName = input.nextLine();

        ClientReader thisGuy = new ClientReader(client);
        thisGuy.connect(userName);
        Thread thisGuyThread = new Thread(thisGuy);
        thisGuyThread.start();


        while(true) {

            System.out.println(">>");
            String message = input.nextLine();

            if (message.equals("/quit")) {
                System.out.println("Are you sure y/n");
                String yesOrNo = input.nextLine();

                if (yesOrNo.equals("y")) {
                    thisGuy.disconnect();
                    System.out.println("Goodbye!");
                    break;
                }
                else if (yesOrNo.equals("n"))
                    continue;
                }

            else if (message.equals("/help")) {
                System.out.println("/help - displays this message\n" +
                        " /quit - quit Chatterbox\n" +
                        " /c <message> - send a message to all currently connected users\n" +
                        " /w <recipient> <message> - send a private message to the recipient\n" +
                        " /list - display a list of currently connected users");
            }

            else if(message.equals("/list")) {
                thisGuy.listUsers();
            }

            else if (message.split(" ")[0].equals("/c")) {
                thisGuy.sendChat("send_chat::" + message.split(" ", 2)[1] + "\n");
            }

            else if (message.split(" ")[0].equals("/w")) {
                String[] splitMessage = message.split(" ", 3);
                thisGuy.sendWhisper("Send_whisper::" + splitMessage[1] + "::" + splitMessage[2] + "\n");
            }

            else {
                System.out.println("Improper input. Please try again");
                continue;
            }

        }
        input.close();
    }


}
