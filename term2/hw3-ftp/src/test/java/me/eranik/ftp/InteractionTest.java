package me.eranik.ftp;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    private static String hostName = "localhost";
    private static int portNumber = 12345;
    private Server server;
    private Thread serverThread;

    private String first = Paths.get("src", "test", "resources", "1").toString() + " true\n";
    private String second = Paths.get("src", "test", "resources", "1", "2").toString() + " true\n";
    private String third = Paths.get("src", "test", "resources", "1", "2", "3") + " true\n";
    private String html = Paths.get("src", "test", "resources", "1", "2", "3", "hello.html") + " false\n";
    private String py = Paths.get("src", "test", "resources", "1", "2", "hello.py") + " false\n";
    private String fourth = Paths.get("src", "test", "resources", "1", "4") + " true\n";
    private String java = Paths.get("src", "test", "resources", "1", "4", "hello.java") + " false\n";
    private String cpp = Paths.get("src", "test", "resources", "1", "hello.cpp") + " false\n";
    private String txt = Paths.get("src", "test", "resources", "hello.txt") + " false\n";



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
        String request = "list " + Paths.get("src", "test", "resources").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "9\n" + first + second + third + html + py + fourth + java + cpp + txt;

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction1() {
        String request = "list " + Paths.get("src", "test", "resources", "1").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "7\n" + second + third + html + py + fourth + java + cpp;

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction2() {
        String request = "list " + Paths.get("src", "test", "resources", "1", "2").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "3\n" + third + html + py;

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction3() {
        String request = "list " + Paths.get("src", "test", "resources", "1", "2", "3").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "1\n" + html;

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunction4() {
        String request = "list " + Paths.get("src", "test", "resources", "1", "4").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = "1\n" + java;

        assertEquals(result.trim(), output.toString().trim());
    }

    @Test
    void testListFunctionFileDoesNotExist() {
        String request = "list " + Paths.get("src", "test", "resources", "abracadabra").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertEquals("0", output.toString().trim());
    }

    @Test
    void testGetFunctionSmallFile() throws IOException {
        String request = "get " + Paths.get("src", "test", "resources", "1", "hello.cpp").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertEquals("File was successfully downloaded to the current directory.", output.toString().trim());

        assertTrue(new File("hello.cpp").exists());

        assertTrue(FileUtils.contentEquals(new File("hello.cpp"),
                new File(Paths.get("src", "test", "resources", "1", "hello.cpp").toString())));

        assertTrue(new File("hello.cpp").delete());
    }

    @Test
    void testGetFunctionBigFile() throws IOException {
        String request = "get " + Paths.get("src", "test", "resources", "hello.txt").toString();

        ByteArrayInputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        assertEquals("File was successfully downloaded to the current directory.", output.toString().trim());

        assertTrue(new File("hello.txt").exists());

        assertTrue(FileUtils.contentEquals(new File("hello.txt"),
                new File(Paths.get("src", "test", "resources", "hello.txt").toString())));

        assertTrue(new File("hello.txt").delete());
    }

}