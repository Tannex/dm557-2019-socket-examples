package dk.imada.ns;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

/**
 * General idea: use datagram sockets to request data from server, then wait for
 * a response and store that in a buffer.
 */
public class TimeClient extends Thread {

    @Override
    public void run() {
        try {
            // Create new socket for sending and recieveing datagram packets. When called
            // without arugments, a port will automatically be selected.
            DatagramSocket socket = new DatagramSocket();

            // Create buffer for sending requests to server.
            byte[] buf = new byte[256];

            // We assume that the server is running locally, so we use the loopback address.
            // This would have to be changed if the time server was external.
            InetAddress address = InetAddress.getLoopbackAddress();

            // Create and send a packet with the empty payload and send it to the time
            // server.
            // If the time server had multiple options, the client would likely have to
            // specify what it wanted from the server through buf.
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);

            // We then prepare a new packet for recieving data from the server, and wait for
            // the data to arrive.
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // Once the packet is recieved, we interpret the data. In this case, we know the
            // server sent a String.
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Time at server: " + received);
            socket.close();
        } catch (Exception e) {
            System.err.println("Exception occured during TimeClient: " + e.getLocalizedMessage());
        }

    }

}