import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

    private final int listenPort = 2019;

    public void start() {

        System.out.println("Starting Calculator server...");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        String inputString;
        String[] tokens;

        try {
            // create server socket
            serverSocket = new ServerSocket(listenPort, 50, InetAddress.getLocalHost());

            // create client socket
            clientSocket = serverSocket.accept();

            // create input steam reader
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // create output stream reader
            writer = new PrintWriter(clientSocket.getOutputStream());

            writer.println("Ask me something !");

            while (!(inputString = reader.readLine()).equals("quit")) {

                tokens = inputString.split(" ");

                switch (tokens[0]) {
                    case "add":
                        writer.println("result : " + (Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2])));
                        break;
                    default:
                }

                writer.println("Something else ?");

            }


        } catch (IOException ex) {

        } finally {
            try {
                reader.close();
            } catch (IOException ex) {

            }
            writer.close();

            try {
                clientSocket.close();
            } catch (IOException ex) {

            }
            try {
                serverSocket.close();
            } catch (IOException ex) {

            }
        }
    }

    public static void main(String[] args) {
        CalculatorServer server = new CalculatorServer();
        server.start();
    }
}
