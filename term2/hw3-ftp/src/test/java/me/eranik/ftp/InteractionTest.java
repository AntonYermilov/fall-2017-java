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
        String request = "1 src/test/resources";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "9\n" +
                "src/test/resources/1 true\n" +
                "src/test/resources/1/2 true\n" +
                "src/test/resources/1/2/3 true\n" +
                "src/test/resources/1/2/3/hello.html false\n" +
                "src/test/resources/1/2/hello.py false\n" +
                "src/test/resources/1/4 true\n" +
                "src/test/resources/1/4/hello.java false\n" +
                "src/test/resources/1/hello.cpp false\n" +
                "src/test/resources/hello.txt false";

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction1() {
        String request = "1 src/test/resources/1";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "7\n" +
                        "src/test/resources/1/2 true\n" +
                        "src/test/resources/1/2/3 true\n" +
                        "src/test/resources/1/2/3/hello.html false\n" +
                        "src/test/resources/1/2/hello.py false\n" +
                        "src/test/resources/1/4 true\n" +
                        "src/test/resources/1/4/hello.java false\n" +
                        "src/test/resources/1/hello.cpp false";

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction2() {
        String request = "1 src/test/resources/1/2";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "3\n" +
                        "src/test/resources/1/2/3 true\n" +
                        "src/test/resources/1/2/3/hello.html false\n" +
                        "src/test/resources/1/2/hello.py false";

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction3() {
        String request = "1 src/test/resources/1/2/3";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "1\n" +
                        "src/test/resources/1/2/3/hello.html false";

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction4() {
        String request = "1 src/test/resources/1/4";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "1\n" +
                        "src/test/resources/1/4/hello.java false";

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunctionFileDoesNotExist() {
        String request = "1 src/test/resources/abracadabra";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertEquals("0", output.toString().trim());
    }

    @Test
    void testGetFunctionSmallFile() throws IOException {
        String request = "2 src/test/resources/1/hello.cpp";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertTrue(new File("hello.cpp").exists());
        assertTrue(FileUtils.contentEquals(new File("hello.cpp"),
                new File("src/test/resources/1/hello.cpp")));

        assertTrue(new File("hello.cpp").delete());
    }

    @Test
    void testGetFunctionBigFile() throws IOException {
        String request = "2 src/test/resources/hello.txt";

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertTrue(new File("hello.txt").exists());
        assertTrue(FileUtils.contentEquals(new File("hello.txt"),
                new File("src/test/resources/hello.txt")));

        assertTrue(new File("hello.txt").delete());
    }

}