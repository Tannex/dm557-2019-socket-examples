package dk.imada.ns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Thread {

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(12000);

            System.out.println("UDPServer: Waiting for packets");

            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String payload = new String(packet.getData(), 0, packet.getLength());
            String responsePayload = payload.toUpperCase();

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            buf = responsePayload.getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        } catch( IOException exception) {
            System.out.println("UDPServer: Error in server: " + exception.getMessage());
        }
    }
}
