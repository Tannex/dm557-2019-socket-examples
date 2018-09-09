package dk.imada.ns;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TCPServer extends Thread {

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(12000);
            Socket socket = serverSocket.accept();

            boolean autoflush = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // read the response
            boolean loop = true;
            StringBuilder sb = new StringBuilder(8096);
            while (loop) {
                if (in.ready()) {
                    int i = 0;
                    while (i != '\n') {
                        i = in.read();
                        sb.append((char) i);
                    }
                    loop = false;
                }
            }
            String payload = sb.toString();
            out.println(payload.toUpperCase() );
            out.flush();
            out.close();
            socket.close();
            serverSocket.close();
        } catch( IOException exception) {
            System.out.println("Error in server: " + exception.getMessage());
        }
    }
}
