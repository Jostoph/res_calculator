package ch.heigvd.res;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class ch.heigvd.res.CalculatorClient
 *
 * Simple TCP client that connects to a CalculatorServer on port 2019
 * and can interact with it by sending commands via user inputs.
 * The client exits disconnects from server and closes the program by
 * sending the command "quit" or "exit".
 *
 * @author Jostoph
 * @version 1.0
 */
public class CalculatorClient {

    // the port number
    private final int connexionPort = 2019;
    // the server ip
    private String serverIP;

    // constructor with chosen IP
    private CalculatorClient(String serverIP) {
        this.serverIP = serverIP;
    }

    // constructor with default IP (localhost)
    private CalculatorClient() throws UnknownHostException {
        this.serverIP = InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Does the entire processing
     */
    private void start() {

        System.out.println("Starting Calculator Client...");

        boolean running = true;

        Socket clientSocket = null;
        BufferedReader userInputReader = null;
        BufferedReader serverResponseReader = null;
        PrintWriter userResponseWriter = null;

        String userInput;
        StringBuilder serverOutput;
        String serverLine;

        try {
            // create server socket
            clientSocket = new Socket(serverIP, connexionPort);

            // create the user input stream reader (what comes from command input)
            userInputReader = new BufferedReader(new InputStreamReader(System.in));
            // create the server output reader (what comes from server)
            serverResponseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // create the output stream writer
            userResponseWriter = new PrintWriter(clientSocket.getOutputStream());

            // processing loop
            while (clientSocket.isConnected() && running) {

                // string buffer for server response lines
                serverOutput = new StringBuilder();

                // read the server message line by line until reception of the "msg_end" line
                serverLine = serverResponseReader.readLine();
                while (!serverLine.equals("msg_end")) {

                    // server has close, ending program
                    if(serverLine.equals("bye !")) {
                        running = false;
                    }
                    // stores received lines in the string buffer (builder)
                    serverOutput.append(serverLine).append("\n");
                    // get next line
                    serverLine = serverResponseReader.readLine();
                }
                // print output to the user
                System.out.print(serverOutput);

                if(running) {
                    // read next user input
                    userInput = userInputReader.readLine();
                    // send the user input to the server
                    userResponseWriter.println(userInput);
                    userResponseWriter.flush();
                }
            }

        } catch (IOException ex) {
            printError(ex);
        } finally {
            try {
                if(userInputReader != null) {
                    userInputReader.close();
                }
                if(serverResponseReader != null) {
                    serverResponseReader.close();
                }
            } catch (IOException ex) {
                printError(ex);
            }

            if(userResponseWriter != null) {
                userResponseWriter.close();
            }

            try {
                if(clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ex) {
                printError(ex);
            }
        }
    }

    /**
     * Prints the error message to the user
     * @param ex the caught exception
     */
    private void printError(Exception ex) {
        System.out.println("Error : " + ex.getMessage() + "\n");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // create new client instance
        CalculatorClient client;
        try {
            if(args.length > 0) {
                client = new CalculatorClient(args[0]);
            } else {
                client = new CalculatorClient();
            }
            // start client
            client.start();
        } catch (UnknownHostException ex) {
            System.out.println("Unknown host : " + ex.getMessage());
        }
    }
}
