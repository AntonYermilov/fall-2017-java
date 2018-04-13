package me.eranik.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Server {

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

    private static class FileTransferProtocol {

        private static final int BUFFER_SIZE = 4096;

        private static String readPath(DataInputStream input) throws IOException {
            StringBuilder path = new StringBuilder();
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                path.append(new String(buffer, 0, read));
            }
            return path.toString();
        }

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

        private static void processListQuery(DataOutputStream output, String path) throws IOException {
            File file = new File(path);
            StringBuilder list = new StringBuilder();
            int size = getDirectoryTree(file, list);
            output.writeInt(size);
            output.write(list.toString().getBytes());
        }

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

        static class FileTransferProtocolException extends IOException {
            FileTransferProtocolException() {
                super("The request does not match the protocol");
            }
        }

    }

}
