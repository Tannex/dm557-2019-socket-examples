package dk.imada.ns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class ServerSocketPortScanner extends Thread {

    // Define variables used to determine port range to scan.
    private int portMin;
    private int portMax;

    /**
     * General idea: Scan ports for active services on local machine, by attempting
     * to create a service listening on each port. If something else is already
     * listening on a specified port, the OS should reject the listen request on
     * that port.
     * 
     * WARNING: You might have to run the program with sudo / admin privileges
     * 
     * @param portMin First port to scan
     * @param portMax Last port to scan
     * @return {@link ServerSocketPortScanner} instance
     */
    public ServerSocketPortScanner(int portMin, int portMax) {
        this.portMin = portMin;
        this.portMax = portMax;
    }

    // When extending the Thread class, you put your code in run
    // much like you would usually do in public static void main (See Main.java).
    @Override
    public void run() {

        // Iterate over the given port range to scan
        for (int port = this.portMin; port <= this.portMax; port++) {

            // We will use the IOException thrown by ServerSocket on listen failure to
            // determine if a service is running on a given port or not.
            // As such, we use a try-catch block
            try {

                // Create socket for port testing
                ServerSocket socket = new ServerSocket();

                // We can get the loopback address as seen below, or manually enter one of the common loopback addresses: "127.0.0.1", "0.0.0.0" or "localhost".
                InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
                SocketAddress scanAddress = new InetSocketAddress(loopbackAddress, port);

                // Attempt the bind
                socket.bind(scanAddress);
                socket.close();

                // Alternatively, the shorthand: (only use one of the options at a time)
                // ServerSocket otherSocket = new ServerSocket(port);
                // otherSocket.close();

                // If the server socket successfully binds, we know that no service is listening on
                // the currently tested port, so we ignore it.
                

            } catch (IOException e) {
                // If an exception is thrown by the server sockets, we can assume that the port
                // is sed, and we print the port number.
                System.out.println("Service listening on port: " + port);
                // System.out.println("No service listening on port: " + port);
            }

        }

    }

}
