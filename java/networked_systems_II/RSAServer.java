package PL5;

import static PL5.MyRSA.plain;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;

public class RSAServer {

    private static ServerSocket servSocket;
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try {
            servSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        do {
            //Wait for client...
            Socket client = servSocket.accept();

            System.out.println("\nNew client accepted.\n");

            //Create a thread to handle communication with
            //this client and pass the constructor for this
            //thread a reference to the relevant socket...
            ClientHandler handler = new ClientHandler(client);

            handler.start();//As usual, this method calls run.
        } while (true);
    }
}

class ClientHandler extends Thread {

    private Socket client;
    private Scanner in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        //Set up reference to associated socket...
        client = socket;

        try {
            in = new Scanner(
                    new InputStreamReader(
                            client.getInputStream()));
            out = new PrintWriter(
                    client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            long received;
            String decry = "";
            while (in.hasNextLong()) {
                //Accept message from client on
                //the socket's input stream...
                received = in.nextLong();
                System.out.println("Encrypted String: " + received);
                //Echo message back to client on
                //the socket's output stream...
                decry = decrypt(received);
                out.println("Encrypted char received: " + received);
                sb.append(decry);
                //Repeat above until 'QUIT' sent by client...
                }
        } finally {
            try {
                System.out.println("Decrypted:" + sb);
                if (client != null) {
                    System.out.println(
                            "Closing down connection...");
                    client.close();
        }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     public static String decrypt(long c) {
        int d = 29;
        int n = 35;
        BigInteger temp = new BigInteger(c + "");
        BigInteger c_d = temp.pow(d);
        BigInteger nbig = new BigInteger(n+"");
        temp = c_d.mod(nbig);
        int m = temp.intValue();
        return "" + plain.charAt(m-1);
    }
}
