package dk.imada.ns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient extends Thread {

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getLoopbackAddress();
            Integer serverPort = 12000;

            DatagramSocket socket = new DatagramSocket();

            System.out.println("Input lowercase sentence:\n");
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, serverPort);
            socket.send(packet);

            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Received:" + received);
        } catch( IOException exception) {
            System.out.println("Error in client: " + exception.getMessage());
        }
    }
}
