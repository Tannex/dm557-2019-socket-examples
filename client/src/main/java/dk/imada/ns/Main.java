package dk.imada.ns;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void printInfo() {
        System.out.println();
        System.out.println("Socket demos");
        System.out.println("Choose from the following");
        System.out.println("1 - UDP example communication");
        System.out.println("2 - TCP example communication");
        System.out.println("3 - start time-server");
        System.out.println("4 - start time-client");
        System.out.println("5 - HTTP GET (assuming server runs from server dir)");
        System.out.println("6 - HTTP POST (assuming server runs from server dir)");
        System.out.println("7 - Socket port scanner");
        System.out.println("8 - ServerSocket port scanner");
        System.out.println("exit - terminate the program");
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printInfo();
            String input = sc.nextLine().toLowerCase();
            switch (input) {
            case "1":
                udpExample();
                break;
            case "2":
                tcpExample();
                break;
            case "3":
                timeServer();
                break;
            case "4":
                timeClient();
                break;
            case "5":
                tryGetMethod();
                break;
            case "6":
                tryPostMethod();
                break;
            case "7":
                socketPortScanExample(sc);
                break;
            case "8":
                serverSocketPortScanExample(sc);
                break;
            case "exit":
                sc.close();
                System.exit(0);
            default:
                System.out.println("Invalid input: " + input);
            }
        }
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
    // Moved into its own file for threading.
    static void timeServer() throws IOException {
        (new TimeServer()).start();
    }

    static void timeClient() {
        TimeClient client = new TimeClient();
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            //TODO: handle exception
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
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    static void socketPortScanExample(Scanner scanner) {

        int portMin = 0;
        int portMax = 0;

        Boolean exit = false;
        Boolean getMin = true, getMax = true;

        // Get portMin
        while (getMin && !exit) {
            System.out.println("Please enter lowest port number to scan:");
            System.out.println("Or \"exit\" to return to previous menu");
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
            case "exit":
                exit = true;
                break;
            default:
                try {
                    portMin = Integer.parseInt(input);
                    getMin = false;
                } catch (Exception e) {
                    System.out.println();
                }
                break;
            }
        }

        // Get portMax
        while (getMax && !exit) {
            System.out.println("Please enter highest port number to scan:");
            System.out.println("Or \"exit\" to return to previous menu");
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
            case "exit":
                exit = true;
                break;
            default:
                try {
                    portMax = Integer.parseInt(input);
                    assert portMin <= portMax;
                    getMax = false;
                } catch (Exception e) {
                    System.out.println();
                }
                break;
            }
        }

        if (!exit && portMin <= portMax) {
            // Parallelize port scanning by starting multiple instances if large range is to
            // be scanned.
            // This is primarily due to socket port scanning being observed to run slowly on
            // Windows.
            ArrayList<SocketPortScanner> portScanners = new ArrayList<SocketPortScanner>((portMax - portMin) / 20);
            for (int i = portMin; i <= portMax; i += 20) {
                int localMin = i;
                int localMax = Math.min(i + 19, portMax);
                System.out.println("Starting scan: " + localMin + "-" + localMax);
                portScanners.add(new SocketPortScanner(localMin, localMax));
            }
            portScanners.forEach((portScanner) -> portScanner.start());
            for (SocketPortScanner portScanner : portScanners) {
                try {
                    portScanner.join();
                } catch (InterruptedException e) {
                    System.out.println("Something went wrong while waiting for port scan to finish");
                }
            }
        }

    }


    static void serverSocketPortScanExample(Scanner scanner) {

        int portMin = 0;
        int portMax = 0;

        Boolean exit = false;
        Boolean getMin = true, getMax = true;

        // Get portMin
        while (getMin && !exit) {
            System.out.println("Please enter lowest port number to scan:");
            System.out.println("Or \"exit\" to return to previous menu");
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
            case "exit":
                exit = true;
                break;
            default:
                try {
                    portMin = Integer.parseInt(input);
                    getMin = false;
                } catch (Exception e) {
                    System.out.println();
                }
                break;
            }
        }

        // Get portMax
        while (getMax && !exit) {
            System.out.println("Please enter highest port number to scan:");
            System.out.println("Or \"exit\" to return to previous menu");
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
            case "exit":
                exit = true;
                break;
            default:
                try {
                    portMax = Integer.parseInt(input);
                    assert portMin <= portMax;
                    getMax = false;
                } catch (Exception e) {
                    System.out.println();
                }
                break;
            }
        }

        if (!exit && portMin <= portMax) {
            // We do not need to parallelize since the OS will almost instantly tell us if
            // we can
            // bind to a port or not.
            System.out.println("Starting scan: " + portMin + "-" + portMax);
            ServerSocketPortScanner serverSocketPortScanner = new ServerSocketPortScanner(portMin, portMax);
            serverSocketPortScanner.start();
            try {
                serverSocketPortScanner.join();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong while waiting for port scan to finish");
            }

        }

    }

}
