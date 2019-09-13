package dk.imada.ns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketPortScanner extends Thread {

    // Define variables used to determine port range to scan.
    private int portMin;
    private int portMax;

    /**
     * General idea: Scan ports for active services on local machine, by attempting
     * to make socket connections to a range of ports on the loopback address.
     * 
     * @param portMin First port to scan
     * @param portMax Last port to scan
     * @return {@link SocketPortScanner} instance
     */
    public SocketPortScanner(int portMin, int portMax) {
        this.portMin = portMin;
        this.portMax = portMax;
    }

    // When extending the Thread class, you put your code in run
    // much like you would usually do in public static void main (See Main.java).
    @Override
    public void run() {

        // Iterate over the given port range to scan
        for (int port = this.portMin; port <= this.portMax; port++) {

            // We will use the IOException thrown by Socket on connection failure to
            // determine if a service is running on a given port or not.
            // As such, we use a try-catch block
            try {

                // Create socket for port testing
                Socket socket = new Socket();

                // We now use the created socket to attempt a connection with the current port,
                // testing if a service is listening on the current port. We can get the
                // loopback address as seen below, or manually enter one of the common loopback
                // addresses: "127.0.0.1", "0.0.0.0" or "localhost".
                InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
                SocketAddress scanAddress = new InetSocketAddress(loopbackAddress, port);

                // Since we are scanning our local computer we can set a quite short timeout
                // In this example we use 50 milliseconds
                int timeout = 50;

                // Attempt the connection
                socket.connect(scanAddress, timeout);
                socket.close();

                // Alternatively, the shorthand:
                // Socket otherSocket = new Socket(InetAddress.getLoopbackAddress(), port);
                // otherSocket.close();

                // If the socket successfully connected, we know that a service is listening on
                // the currently tested port, and we print it.
                System.out.println("Service listening on port: " + port);

            } catch (IOException e) {
                // If an exception is thrown by the client sockets, we can assume that the port
                // is unused, and we choose to do nothing. Alternatively, we can print port
                // closed:
                // System.out.println("No service listening on port: " + port);
            }

        }

    }

}
