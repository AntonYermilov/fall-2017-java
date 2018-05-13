package me.eranik.ftp;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String request = Paths.get("src", "test", "resources").toString();

        String[] response = Client.processListQuery(request);

        String result = first + txt;

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction1() {
        String request = Paths.get("src", "test", "resources", "1").toString();

        String[] response = Client.processListQuery(request);

        String result = second + fourth + cpp;

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction2() {
        String request = Paths.get("src", "test", "resources", "1", "2").toString();

        String[] response = Client.processListQuery(request);

        String result = third + py;

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction3() {
        String request = Paths.get("src", "test", "resources", "1", "2", "3").toString();

        String[] response = Client.processListQuery(request);

        String result = html;

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunction4() {
        String request =Paths.get("src", "test", "resources", "1", "4").toString();

        String[] response = Client.processListQuery(request);

        String result = java;

        assertArrayEquals(result.trim().split("\n"), response);
    }

    @Test
    void testListFunctionFileDoesNotExist() {
        String request = Paths.get("src", "test", "resources", "abracadabra").toString();

        String[] response = Client.processListQuery(request);

        assertArrayEquals(new String[]{""}, response);
    }

    @Test
    void testGetFunctionSmallFile() throws IOException {
        String request = Paths.get("src", "test", "resources", "1", "hello.cpp").toString();
        String pathToFile = Paths.get("src", "test", "resources", "tmp").toString();

        Client.processGetQuery(request, pathToFile);

        assertTrue(new File(pathToFile).exists());

        assertTrue(FileUtils.contentEquals(new File(pathToFile), new File(request)));

        assertTrue(new File(pathToFile).delete());
    }

    @Test
    void testGetFunctionBigFile() throws IOException {
        String request = Paths.get("src", "test", "resources", "hello.txt").toString();
        String pathToFile = Paths.get("src", "test", "resources", "tmp").toString();

        Client.processGetQuery(request, pathToFile);

        assertTrue(new File(pathToFile).exists());

        assertTrue(FileUtils.contentEquals(new File(pathToFile), new File(request)));

        assertTrue(new File(pathToFile).delete());
    }

}