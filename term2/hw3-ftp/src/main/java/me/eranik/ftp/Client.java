package me.eranik.ftp;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client implementation. Provides possibility to list files in the specified directory on server
 * and to download files from server.
 */
public class Client {

    private final int BUFFER_SIZE = 4096;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    private String hostName;
    private int portNumber;

    private Scanner reader;
    private PrintWriter writer;

    private enum QueryStatus {
        SUCCESS, CONNECTION_ERROR, RESPONSE_ERROR, FILE_NOT_EXISTS_ERROR
    }


    /**
     * Runs client. Queries are sent to the server with the specified port and hostname.
     * @param args list of arguments: first argument contains hostname, second argument contains port number
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Expected two arguments: <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];

        int portNumber = -1;
        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Port number should be an integer.");
            System.exit(2);
        }

        new Client(hostName, portNumber, System.in, System.out).runClient();
    }

    /**
     * Creates clients that connects to the specified host on the specified port.
     * Uses inputStream and outputStream to read queries and write responses.
     * @param hostName address of host to connect
     * @param portNumber number of port to connect
     * @param inputStream stream from which we can read queries
     * @param outputStream stream to which we would write responses
     */
    public Client(@NotNull String hostName, int portNumber, @NotNull InputStream inputStream,
                  @NotNull OutputStream outputStream) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.reader = new Scanner(inputStream);
        this.writer = new PrintWriter(outputStream, true);
    }

    /**
     * Runs client. It allows to send two types of queries:
     * <1: Int> <path: String>       List of all files in the specified directory.
     * <2: Int> <path: String>       Download file from server.
     */
    public void runClient() {
        while (reader.hasNext()) {
            String query = reader.nextLine();
            String[] args = query.split(" ");

            if (args.length == 1 && args[0].equals("exit")) {
                System.exit(0);
            }

            if (args.length != 2 || !args[0].equals("list") && !args[0].equals("get")) {
                writer.println("Incorrect query format.");
                writer.println("list <path: String>       List of all files in the specified directory.");
                writer.println("get  <path: String>       Download file from server.");
                writer.println("exit                      Exit program.");
                continue;
            }

            QueryType type = QueryType.getEnum(args[0]);
            String path = args[1].trim();

            if (type == null) {
                writer.println("Unexpected error occurred while reading query type. Try once more");
                continue;
            }

            QueryStatus status = processQuery(type, path);
            switch (status) {
                case RESPONSE_ERROR:
                    writer.println("Error occurred while getting response from server.");
                    writer.println("Check whether your query is correct or try again later.");
                    break;
                case CONNECTION_ERROR:
                    writer.println("Can't connect to server. Try again later.");
                    break;
                case FILE_NOT_EXISTS_ERROR:
                    writer.println("Specified file was not found on server.");
                    writer.println("Make sure that you have entered correct file name and that it is not a directory");
                    break;
                case SUCCESS:
                    if (type.equals(QueryType.getQuery)) {
                        writer.println("File was successfully downloaded to the current directory.");
                    }
                    break;
            }
        }
    }

    /**
     * Sends specified query to the server and processes server's response.
     * @param type type of query
     * @param path path to the file or directory
     */
    private QueryStatus processQuery(QueryType type, @NotNull String path) {
        try (Socket socket = new Socket(hostName, portNumber);
             DataInputStream dataInput = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream())
        ) {
            dataOutput.writeInt(type.getValue());
            dataOutput.write(path.getBytes());
            dataOutput.flush();

            socket.shutdownOutput();

            try {
                if (type.equals(QueryType.listQuery)) {
                    listFiles(dataInput);
                } else {
                    try {
                        saveFile(dataInput, new File(path).getName());
                    } catch (FileNotExistsException e) {
                        return QueryStatus.FILE_NOT_EXISTS_ERROR;
                    }
                }
            } catch (IOException e) {
                return QueryStatus.RESPONSE_ERROR;
            }
        } catch (IOException e) {
            return QueryStatus.CONNECTION_ERROR;
        }
        return QueryStatus.SUCCESS;
    }

    /**
     * Processes response to the first type of query. Prints list of files in the directory from query.
     * @param dataInput stream that allows to read server's response
     * @throws IOException if any error occurred while getting response from server
     */
    private void listFiles(@NotNull DataInputStream dataInput) throws IOException {
        int size = dataInput.readInt();
        StringBuilder result = new StringBuilder();
        for (int read = dataInput.read(buffer); read != -1; read = dataInput.read(buffer)) {
            result.append(new String(buffer, 0, read));
        }

        writer.println(size);
        writer.println(result.toString());
    }

    /**
     * Processes response to the second type of query. Downloads file from server and saves it locally to
     * the file with the same name.
     * @param dataInput stream that allows to read server's response
     * @param filename name of file to be saved locally
     * @throws IOException if any error occurred while getting response from server
     */
    private void saveFile(@NotNull DataInputStream dataInput, @NotNull String filename) throws IOException {
        long size = dataInput.readLong();

        if (size < 0) {
            throw new FileNotExistsException();
        }
        if (size == 0) {
            return;
        }

        try (DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(new File(filename)))) {
            for (int read = dataInput.read(buffer); read != -1; read = dataInput.read(buffer)) {
                size -= read;
                dataOutput.write(buffer, 0, read);
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
    private class FileTransferProtocolException extends IOException {
        private FileTransferProtocolException() {
            super("Some data lost or you received some extra data while getting file from server");
        }
    }

    /**
     * Is thrown is specified file was not found on server.
     */
    private class FileNotExistsException extends IOException {
        private FileNotExistsException() {
            super("Specified file was not found on server");
        }
    }

}
