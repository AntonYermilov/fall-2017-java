package me.eranik.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Server implementation. Provides possibility to listen connections from outside
 * and process queries from clients.
 */
public class Server {

    /**
     * Runs server on the specified port. Listens to clients and processes queries from them.
     * Also allows you to kill server by writing a special command.
     * @param args list of arguments: first argument contains port number
     */
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);

        Thread server = new Thread(() -> runServer(portNumber));
        server.setDaemon(false);
        server.start();


        try (Scanner input = new Scanner(System.in);
             PrintWriter output = new PrintWriter(System.out, true)
        ) {
            while (true) {
                output.println("Type \"exit\" to stop server:");
                String query = input.nextLine();
                if (query.equals("exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Unexpected error occurred");
            System.err.println(e.getMessage());
        }
        server.interrupt();
    }

    /**
     * Runs server on the specified port and listens for connections.
     * @param portNumber number of port to start server on
     */
    public static void runServer(int portNumber) {
        try (ServerSocket server = new ServerSocket(portNumber);
             PrintWriter output = new PrintWriter(System.out, true)
        ) {
            output.println("Server " + server.getInetAddress() + " is running on port " + portNumber);
            server.setSoTimeout(20000);

            while (!Thread.interrupted()) {
                try (Socket connection = server.accept()) {
                    output.println(connection.getInetAddress() + " connected");

                    FileTransferProtocol.processConnection(connection);

                    output.println(connection.getInetAddress() + " disconnected");
                } catch (SocketTimeoutException skip) {
                } catch (IOException e) {
                    System.err.println("Error occurred when listening for a connection");
                    System.err.println(e.getMessage());
                }
            }

            output.println("Server is closed");
        } catch (IOException e) {
            System.err.println("Error occurred when opening socket on port " + portNumber);
            System.err.println(e.getMessage());
        }
    }

    /**
     * Provides file transfer protocol implementation for processing queries and forming responses to them.
     */
    private static class FileTransferProtocol {

        private static final int BUFFER_SIZE = 4096;

        /**
         * Reads path from the connection's query.
         * @param input stream that allows to read client's query
         * @return path to the file or directory from the client's query
         * @throws IOException if any other error occurred while listening for connection
         */
        private static String readPath(DataInputStream input) throws IOException {
            StringBuilder path = new StringBuilder();
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                path.append(new String(buffer, 0, read));
            }
            return path.toString();
        }

        /**
         * Prints tree of parent's directory to the string.
         * @param parent specified parent's directory
         * @param list list to store files and directories from the directory tree of specified directory
         * @return number of files and directories in he directory tree of specified directory
         */
        private static int getDirectoryTree(File parent, StringBuilder list) {
            int size = 0;
                if (parent.isDirectory()) {
                for (File child : parent.listFiles()) {
                    list.append(child.getPath());
                    list.append(' ');
                    list.append(child.isDirectory());
                    list.append('\n');
                    size += 1 + getDirectoryTree(child, list);
                }
            }
            return size;
        }

        /**
         * Sends list of files and directories from the directory tree of specified directory to the client.
         * @param output stream that allows to write response to client
         * @param path path to the specified directory
         * @throws IOException if any other error occurred while listening for connection
         */
        private static void processListQuery(DataOutputStream output, String path) throws IOException {
            File file = new File(path);
            StringBuilder list = new StringBuilder();
            int size = getDirectoryTree(file, list);
            output.writeInt(size);
            output.write(list.toString().getBytes());
        }

        /**
         * Sends specified file to the client.
         * @param output stream that allows to write response to client
         * @param path path to the specified directory
         * @throws IOException if any other error occurred while listening for connection
         */
        private static void processGetQuery(DataOutputStream output, String path) throws IOException {
            File file = new File(path);
            if (!file.isFile()) {
                output.writeLong(0);
                return;
            }
            output.writeLong(file.length());
            try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                    output.write(buffer, 0, read);
                }
            }
        }

        /**
         * Processes query from the specified connection and sends response to it.
         * @param connection specified connection to process query from
         * @throws FileTransferProtocolException if query does not satisfy file transfer protocol
         * @throws IOException if any other error occurred while listening for connection
         */
        static void processConnection(Socket connection) throws IOException {
            try (DataInputStream input = new DataInputStream(connection.getInputStream());
                 DataOutputStream output = new DataOutputStream(connection.getOutputStream())
            ) {
                int type = input.readInt();
                String path = readPath(input);

                if (type == 1) {
                    processListQuery(output, path);
                    output.flush();
                    return;
                }
                if (type == 2) {
                    processGetQuery(output, path);
                    output.flush();
                    return;
                }

                throw new FileTransferProtocolException();
            }
        }

        /**
         * Is thrown when client's query does not satisfy the file transfer protocol.
         */
        static class FileTransferProtocolException extends IOException {
            FileTransferProtocolException() {
                super("The query does not match the protocol");
            }
        }

    }

}
