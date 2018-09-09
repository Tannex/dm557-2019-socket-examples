package dk.imada.ns;

import java.io.*;
import java.net.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
/*
        tryGetMethod();
        tryPostMethod();
        timeServer();
        udpExample();
*/
        tcpExample();
    }

    public static void tcpExample() {
        (new TCPServer()).start();
        (new TCPClient()).start();
    }

    public static void udpExample() {
        (new UDPServer()).start();
        (new UDPClient()).start();
    }

    // Demo using netcat: netcat -u localhost 4445
    static void timeServer() throws IOException {
        DatagramSocket socket = new DatagramSocket(4445);
        int count = 0;
        while(count < 10) {
            System.out.println("Waiting for packets");
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            buf = new Date().toString().getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            count++;
        }
    }

    static void tryGetMethod() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);

        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(

        new InputStreamReader(socket.getInputStream()));
        out.println("GET / HTTP/1.1");
        out.println();

        // read the response
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        socket.close();

    }

    static void tryPostMethod() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);

        String data = "{\"input\":\"What a POST party we have\"}";

        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(

        new InputStreamReader(socket.getInputStream()));
        out.write("POST / HTTP/1.0\r\n");
        out.write("Content-Length: " + data.length() + "\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write(data);
        out.flush();

        // read the response
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        socket.close();

    }

}
