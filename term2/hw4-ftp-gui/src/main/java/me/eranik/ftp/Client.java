package me.eranik.ftp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
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
     * Creates clients that connects to the specified host on the specified port.
     * Uses inputStream and outputStream to read queries and write responses.
     * @param hostName address of host to connect
     * @param portNumber number of port to connect
     */
    public static void initialize(@NotNull String hostName, int portNumber) {
        Client.hostName = hostName;
        Client.portNumber = portNumber;
        Arrays.fill(buffer, (byte) 0);
    }

    /**
     * Sends list query to the server and processes server's response.
     * @param serverPath path to the file or directory on server
     * @return list of files and directories in the directory that satisfies specified path
     */
    public static @Nullable String[] processListQuery(@NotNull String serverPath) {
        try (Socket socket = new Socket(hostName, portNumber);
             DataInputStream dataInput = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream())
        ) {
            dataOutput.writeInt(QueryType.listQuery.getValue());
            dataOutput.write(serverPath.getBytes());
            dataOutput.flush();

            socket.shutdownOutput();

            try {
                return listFiles(dataInput);
            } catch (IOException e) {
                System.err.println("Error occurred while getting server's response");
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error occurred while connecting to server");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Sends list query to the server and processes server's response.
     * @param serverPath path to the file or directory on server
     * @return {@code true} if file was successfully downloaded; {@code false} otherwise
     */
    public static boolean processGetQuery(@NotNull String serverPath, @NotNull String localPath) {
        try (Socket socket = new Socket(hostName, portNumber);
             DataInputStream dataInput = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream())
        ) {
            dataOutput.writeInt(QueryType.getQuery.getValue());
            dataOutput.write(serverPath.getBytes());
            dataOutput.flush();

            socket.shutdownOutput();

            try {
                return saveFile(dataInput, localPath);
            } catch (IOException e) {
                System.err.println("Error occurred while getting server's response");
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error occurred while connecting to server");
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Processes response to the first type of query. Returns list of files in the directory from query.
     * @param dataInput stream that allows to read server's response
     * @return list of files in the directory from query
     * @throws IOException if any error occurred while getting response from server
     */
    private static String[] listFiles(@NotNull DataInputStream dataInput) throws IOException {
        int size = dataInput.readInt();
        StringBuilder result = new StringBuilder();
        for (int read = dataInput.read(buffer); read != -1; read = dataInput.read(buffer)) {
            result.append(new String(buffer, 0, read));
        }

        if (size == 0) {
            return new String[]{""};
        } else {
            return result.toString().trim().split("\n");
        }
    }

    /**
     * Processes response to the second type of query. Downloads file from server and saves it locally to
     * the file with the same name.
     * @param dataInput stream that allows to read server's response
     * @param filename name of file to be saved locally
     * @return {@code true} if file was successfully saved; {@code false} otherwise
     * @throws IOException if any error occurred while getting response from server
     */
    private static boolean saveFile(@NotNull DataInputStream dataInput, @NotNull String filename) throws IOException {
        long size = dataInput.readLong();
        if (size == 0) {
            return false;
        }

        try (DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(new File(filename)))) {
            for (int read = dataInput.read(buffer); read != -1; read = dataInput.read(buffer)) {
                size -= read;
                dataOutput.write(buffer, 0, read);
            }
            if (size != 0) {
                throw new FileTransferProtocolException();
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File " + filename + " cannot be created or you don't have permissions to write to it");
            System.err.println(e.getMessage());
        }
        return false;
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
