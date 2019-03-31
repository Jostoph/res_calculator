package ch.heigvd.res;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class ch.heigvd.res.CalculatorServer
 *
 * Simple TCP server that binds a server socket on port 2019 and waits that a client connects.
 * When the connection with the client is established (supports only one client) the server asks
 * the client to enter a command.
 * The client can ask the server to solve some really simple computations and send back the answer, and
 * exit (end the program) by entering the "quit" or "exit" command.
 *
 * @author Jostoph
 * @version 1.0
 *
 * Based on the StreamingTimeServer by Olivier Liechti (HEIG-VD RES Cours)
 */
public class CalculatorServer {

    // port number
    private final int listenPort = 2019;

    /**
     * Does the entire processing
     */
    public void start() {

        System.out.println("Starting Calculator server...");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        // String to store client inputs
        String inputString;
        // tokens from client commands
        String[] tokens;
        // state of the server
        boolean running = true;

        try {
            // create server socket
            serverSocket = new ServerSocket(listenPort);

            // create client socket and wait for client (supports only a single client)
            System.out.println("Waiting for a client...");

            clientSocket = serverSocket.accept();

            System.out.println("Client connected");

            // create input stream reader
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // create output stream writer
            writer = new PrintWriter(clientSocket.getOutputStream());

            // first operand
            double a;
            // second operand
            double b;

            writer.println("Welcome on the server !\n");
            writer.println("What do you want to compute ? Type : (help) to have the list of commands");
            send(writer);

            // processing loop, loop until client quits (or fatal error)
            while (running) {
                // wait for client input and read line
                inputString = reader.readLine();
                // split input into tokens
                tokens = inputString.split(" ");

                // parse command
                if(tokens.length == 1) {
                    switch (tokens[0]) {
                        case "" :
                            break;
                        case "quit" :
                            running = false;
                            break;
                        case "exit" :
                            running = false;
                            break;
                        case "help" :
                            printHelp(writer);
                            break;
                        default :
                    }
                } else if(tokens.length == 3) {
                    try {
                        a = Double.parseDouble(tokens[1]);
                        b = Double.parseDouble(tokens[2]);

                        switch (tokens[0]) {
                            case "add" :
                                writer.println(tokens[1] + " + " + tokens[2] + " = " + (a + b) + "\n");
                                break;
                            case "sub" :
                                writer.println(tokens[1] + " - " + tokens[2] + " =  " + (a - b) + "\n");
                                break;
                            case "mul" :
                                writer.println(tokens[1] + " * " + tokens[2] + " =  " + (a * b) + "\n");
                                break;
                            case "div" :
                                writer.println(tokens[1] + " / " + tokens[2] + " =  " + (a / b) + "\n");
                                break;
                            default:
                                invalidCMD(writer);
                        }
                    } catch (NumberFormatException nfe) {
                        invalidArguments(writer);
                    }
                } else {
                    invalidCMD(writer);
                }

                if(running) {
                    writer.println("Enter another command (quit) to exit :");
                } else {
                    writer.println("\nbye !");
                }
                send(writer);
            }


        } catch (IOException ex) {
            printError(ex);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                printError(ex);
            }
            if(writer != null) {
                writer.close();
            }

            try {
                if(clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ex) {
                printError(ex);
            }
            try {
                if(serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                printError(ex);
            }
        }
        // server stop
        System.out.println("Stopping Calculator server...");
    }

    /**
     * Add a message to tell the client that it is the last line
     * and flushes the writer
     * @param writer the output stream writer
     */
    private void send(PrintWriter writer) {
        writer.println("\nmsg_end");
        writer.flush();
    }

    /**
     * Informs the client that command he entered is invalid
     * @param writer the output stream writer
     */
    private void invalidCMD(PrintWriter writer) {
        writer.println("Invalid command ! Please type (help) to get the commands list.");
    }

    /**
     * Informs the client that the arguments he entered are invalid
     * @param writer the output stream writer
     */
    private void invalidArguments(PrintWriter writer) {
        writer.println("Invalid arguments ! Please be sure to enter legit numbers !");
    }

    /**
     * Prints the error message in the server shell in case of Exception
     * @param ex the caught exception
     */
    private void printError(Exception ex) {
        System.out.println("Error : " + ex.getMessage() + "\n");
    }

    /**
     * Sends the command list to the client
     * @param writer the output stream writer
     */
    private void printHelp(PrintWriter writer) {
        StringBuilder sb = new StringBuilder("---- help ----\n\n");

        sb.append("help : this page\n");
        sb.append("quit : exit the program\n");
        sb.append("exit : same as quit\n\n");

        sb.append("add a b : computes a + b\n");
        sb.append("sub a b : computes a - b\n");
        sb.append("mul a b : computes a * b\n");
        sb.append("div a b : computes a / b\n");

        writer.println(sb);
    }

    /**
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        // create a new server instance
        CalculatorServer server = new CalculatorServer();
        // start server
        server.start();
    }
}
