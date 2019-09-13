package dk.imada.ns;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * General idea: create a service which uses Datagram sockets and packets for
 * sending the current server time to a requesting client. The client should
 * also use Datagrams for communication.
 */
public class TimeServer extends Thread {

    @Override
    public void run() {
        try {

            // Create a DatagramSocket and bind it to port 4445.
            // If port 4445 is already in use this will fail.
            // It is important to specify a port when running server code so clients know
            // where the service can be found. (Usually clients initiate the communication).
            DatagramSocket socket = new DatagramSocket(4445);

            // The server will shut down after 10 requests.
            int count = 0;
            while (count < 10) {

                System.out.println("Server waiting for packets");

                // Create buffer for storing request data.
                byte[] buf = new byte[256];

                // Create Datagram packet for recieving packets into buffer.
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                // Wait until we recieve a request
                socket.receive(packet);

                // Get the IP and port of requesting client
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                // Create a new buffer containing the time string, and send it to the requesting
                // client.
                buf = new Date().toString().getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

                count++;
            }
            socket.close();
            System.out.println();
            System.out.println("Timer Server closed.");
            System.out.println();
        } catch (IOException e) {
            System.err.println("Exception occured: " + e.getLocalizedMessage());
        }

    }

}