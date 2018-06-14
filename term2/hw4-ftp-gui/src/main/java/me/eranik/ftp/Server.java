package me.eranik.ftp;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Server implementation. Provides possibility to listen connections from outside
 * and process queries from clients.
 */
public class Server {

    private Logger logger = Logger.getGlobal();
    private int portNumber;
    private boolean running;

    /**
     * Runs server on the specified port. Listens to clients and processes queries from them.
     * Also allows you to kill server by writing a special command.
     * @param args list of arguments: first argument contains port number
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected 1 argument: <port number>");
            System.exit(1);
        }

        int portNumber = -1;
        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Port number should be an integer.");
            System.exit(2);
        }

        final int finalPortNumber = portNumber;

        Thread server = new Thread(() -> new Server(finalPortNumber).runServer());
        server.setDaemon(false);
        server.start();


        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("Type \"exit\" to stop server:");
                String query = input.nextLine();
                if (query.equals("exit")) {
                    break;
                }
            }
        }
        server.interrupt();
    }

    /**
     * Creates server on the specified port.
     * @param portNumber number of port to start server on
     */
    public Server(int portNumber) {
        this.portNumber = portNumber;
        this.running = false;
    }

    /**
     * Runs server on the specified port and listens for connections.
     */
    public void runServer() {
        try (ServerSocket server = new ServerSocket(portNumber)) {
            logger.info("Server " + server.getInetAddress() + " is running on port " + portNumber);
            server.setSoTimeout(2000);

            running = true;
            while (!Thread.interrupted()) {
                try {
                    Socket connection = server.accept();
                    logger.info(connection.getInetAddress() + " connected");

                    Thread thread = new Thread(() -> {
                        try {
                            FileTransferProtocol.processConnection(connection);
                            connection.close();
                        } catch (IOException e) {
                            logger.warning("Error occurred when listening for connection\n" + e.getMessage());
                        }
                    });
                    thread.setDaemon(false);
                    thread.start();

                    logger.info(connection.getInetAddress() + " disconnected");
                } catch (SocketTimeoutException skip) {
                } catch (IOException e) {
                    logger.warning("Error occurred when accepting connection\n" + e.getMessage());
                }
            }
            running = false;

            logger.info("Server is closed");
        } catch (IOException e) {
            logger.warning("Error occurred when opening socket on port " + portNumber + "\n" + e.getMessage());
        }
    }

    /**
     * Checks if server is running.
     * @return {@code true} if server is running; {@code false} otherwise
     */
    public boolean isRunning() {
        return running;
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
        private static String readPath(@NotNull DataInputStream input) throws IOException {
            StringBuilder path = new StringBuilder();
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                path.append(new String(buffer, 0, read));
            }
            return path.toString();
        }

        /**
         * Prints list of parent's subdirectories to the string.
         * @param parent specified parent's directory
         * @param list list to store files and directories from the specified directory
         * @return number of files and directories in the specified directory
         */
        private static int getDirectories(@NotNull File parent, @NotNull StringBuilder list) {
            int size = 0;
            if (parent.isDirectory()) {
                File[] children = parent.listFiles();
                Arrays.sort(children, Comparator.comparing(File::getName));

                for (File child : children) {
                    list.append(child.getPath());
                    list.append(' ');
                    list.append(child.isDirectory());
                    list.append('\n');
                    size++;
                }
            }
            return size;
        }

        /**
         * Sends list of files and directories from the specified directory to the client.
         * @param output stream that allows to write response to client
         * @param path path to the specified directory
         * @throws IOException if any other error occurred while listening for connection
         */
        private static void processListQuery(@NotNull DataOutputStream output, @NotNull String path) throws IOException {
            File file = new File(path);
            StringBuilder list = new StringBuilder();
            int size = getDirectories(file, list);
            output.writeInt(size);
            output.write(list.toString().getBytes());
        }

        /**
         * Sends specified file to the client.
         * @param output stream that allows to write response to client
         * @param path path to the specified directory
         * @throws IOException if any other error occurred while listening for connection
         */
        private static void processGetQuery(@NotNull DataOutputStream output, @NotNull String path) throws IOException {
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
        static void processConnection(@NotNull Socket connection) throws IOException {
            try (DataInputStream input = new DataInputStream(connection.getInputStream());
                 DataOutputStream output = new DataOutputStream(connection.getOutputStream())
            ) {
                int type = input.readInt();
                String path = readPath(input);

                if (type == QueryType.listQuery.getValue()) {
                    processListQuery(output, path);
                    output.flush();
                    return;
                }
                if (type == QueryType.getQuery.getValue()) {
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
