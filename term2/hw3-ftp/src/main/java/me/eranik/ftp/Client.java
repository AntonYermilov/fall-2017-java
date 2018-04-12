package me.eranik.ftp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int BUFFER_SIZE = 4096;
    private static final byte[] buffer = new byte[BUFFER_SIZE];

    private static String hostName;
    private static int portNumber;

    public static void main(String[] args) {
        hostName = "0.0.0.0";
        portNumber = 8888;
        runClient();
    }

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

                String path = input.next();
                processQuery(Integer.parseInt(type), path);
            }
        }
    }

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

    private static class FileTransferProtocolException extends IOException {
        private FileTransferProtocolException() {
            super("Some data lost or you received some extra data while getting file from server");
        }
    }

}
