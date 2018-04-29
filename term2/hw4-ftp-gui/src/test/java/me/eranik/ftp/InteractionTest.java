package me.eranik.ftp;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    private static String hostName = "localhost";
    private static int portNumber = 12345;
    private Server server;
    private Thread serverThread;

    @BeforeEach
    void setUp() throws InterruptedException {
        serverThread = new Thread(() -> {
            server = new Server(portNumber);
            server.runServer();
        });
        serverThread.setDaemon(true);
        serverThread.start();

        while (server == null || !server.isRunning()) {
            Thread.sleep(100);
        }

        Client.initialize(hostName, portNumber);
    }

    @AfterEach
    void takeDown() throws InterruptedException {
        while (server.isRunning()) {
            serverThread.interrupt();
            Thread.sleep(100);
        }
    }

    @Test
    void testListFunction0() {
        String request = "src/test/resources";

        String[] response = Client.processListQuery(request);

        String result = "src/test/resources/1 true\n" +
                        "src/test/resources/hello.txt false";

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction1() {
        String request = "src/test/resources/1";

        String[] response = Client.processListQuery(request);

        String result = "src/test/resources/1/2 true\n" +
                        "src/test/resources/1/4 true\n" +
                        "src/test/resources/1/hello.cpp false";

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction2() {
        String request = "src/test/resources/1/2";

        String[] response = Client.processListQuery(request);

        String result = "src/test/resources/1/2/3 true\n" +
                        "src/test/resources/1/2/hello.py false";

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction3() {
        String request = "src/test/resources/1/2/3";

        String[] response = Client.processListQuery(request);

        String result = "src/test/resources/1/2/3/hello.html false";

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction4() {
        String request = "src/test/resources/1/4";

        String[] response = Client.processListQuery(request);

        String result = "src/test/resources/1/4/hello.java false";

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunctionFileDoesNotExist() {
        String request = "src/test/resources/abracadabra";

        String[] response = Client.processListQuery(request);

        assertArrayEquals(new String[]{""}, response);
    }

    @Test
    void testGetFunctionSmallFile() throws IOException {
        String request = "src/test/resources/1/hello.cpp";

        Client.processGetQuery(request, "src/test/resources/tmp");

        assertTrue(new File("src/test/resources/tmp").exists());
        assertTrue(FileUtils.contentEquals(new File("src/test/resources/tmp"),
                new File("src/test/resources/1/hello.cpp")));

        assertTrue(new File("src/test/resources/tmp").delete());
    }

    @Test
    void testGetFunctionBigFile() throws IOException {
        String request = "src/test/resources/hello.txt";

        Client.processGetQuery(request, "src/test/resources/tmp");

        assertTrue(new File("src/test/resources/tmp").exists());
        assertTrue(FileUtils.contentEquals(new File("src/test/resources/tmp"),
                new File("src/test/resources/hello.txt")));

        assertTrue(new File("src/test/resources/tmp").delete());
    }

}