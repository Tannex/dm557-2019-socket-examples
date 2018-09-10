package dk.imada.ns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TCPClient extends Thread {

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 12000);

            boolean autoflush = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            System.out.println("TCPClient: We fake the input of a message: 'Hello DM557!'\n");
            String message = "'Hello DM557! (TCP sockets here)'";

            out.println(message);
            out.println();
            out.flush();

            // read the response
            String response = in.readLine();
            System.out.println("TCPClient: Received:" + response);
            out.close();
            socket.close();

        } catch( IOException exception) {
            System.out.println("TCPClient: Error in client: " + exception.getMessage());
        }
    }
}
