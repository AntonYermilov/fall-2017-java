package me.eranik.ftp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client implementation. Provides possibility to list files in the specified directory on server
 * and to download files from server.
 */
public class Client {

    private static final int BUFFER_SIZE = 4096;
    private static final byte[] buffer = new byte[BUFFER_SIZE];

    private static String hostName;
    private static int portNumber;

    /**
     * Runs client. Queries are sent to the server with the specified port and hostname.
     * @param args List of arguments. First argument contains hostname, second argument contains port number.
     */
    public static void main(String[] args) {
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        runClient();
    }

    /**
     * Runs client. It allows to send two types of queries:
     * <1: Int> <path: String>       List of all files in the specified directory.
     * <2: Int> <path: String>       Download file from server.
     */
    public static void runClient() {
        try (Scanner input = new Scanner(new InputStreamReader(System.in))) {
            while (input.hasNext()) {
                String type = input.next();
                if (type.equals("exit")) {
                    System.exit(0);
                }

                if (!type.equals("1") && !type.equals("2") || !input.hasNext()) {
                    System.out.println("Incorrect query format.");
                    System.out.println("<1: Int> <path: String>       List of all files in the specified directory.");
                    System.out.println("<2: Int> <path: String>       Download file from server.");
                    System.out.println("exit                          Exit program.");
                    continue;
                }

                String path = input.nextLine().trim();
                processQuery(Integer.parseInt(type), path);
            }
        }
    }

    /**
     * Sends specified query to the server and processes server's response.
     * @param type type of query
     * @param path path to the file or directory
     */
    private static void processQuery(int type, String path) {
        try (Socket socket = new Socket(hostName, portNumber);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeInt(type);
            output.write(path.getBytes());
            output.flush();

            socket.shutdownOutput();

            try {
                if (type == 1) {
                    listFiles(input);
                } else {
                    saveFile(input, new File(path).getName());
                }
            } catch (IOException e) {
                System.err.println("Error occurred while getting server's response");
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error occurred while connecting to server");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Processes response to the first type of query. Prints list of files in the directory from query.
     * @param input stream that allows to read server's response
     * @throws IOException if any error occurred while getting response from server
     */
    private static void listFiles(DataInputStream input) throws IOException {
        int size = input.readInt();
        StringBuilder result = new StringBuilder();
        for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
            result.append(new String(buffer, 0, read));
        }

        System.out.println(size);
        System.out.println(result.toString());
        System.out.flush();
    }

    /**
     * Processes response to the second type of query. Downloads file from server and saves it locally to
     * the file with the same name.
     * @param input stream that allows to read server's response
     * @param filename name of file to be saved locally
     * @throws IOException if any error occurred while getting response from server
     */
    private static void saveFile(DataInputStream input, String filename) throws IOException {
        long size = input.readLong();
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(filename)))) {
            for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                size -= read;
                output.write(buffer, 0, read);
            }
            if (size != 0) {
                throw new FileTransferProtocolException();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + filename + " cannot be created or you don't have permissions to write to it");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Is thrown when server's response does not satisfy the file transfer protocol.
     */
    private static class FileTransferProtocolException extends IOException {
        private FileTransferProtocolException() {
            super("Some data lost or you received some extra data while getting file from server");
        }
    }

}
