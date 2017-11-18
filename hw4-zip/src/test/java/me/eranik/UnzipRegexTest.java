package me.eranik;

import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class UnzipRegexTest {

    final String source = Paths.get("src", "test", "resources", "source").toString();
    final String destination = Paths.get("src", "test", "resources", "destination").toString();
    final String regex = ".*\\.txt";

    @BeforeEach
    void createTemporaryFiles() throws IOException {
        Paths.get(source).toFile().mkdirs();
        Paths.get(destination).toFile().mkdirs();

        createFirstArchive();
        createSecondArchive();
    }

    @AfterEach
    void deleteTemporaryFiles() throws IOException {
        FileUtils.deleteDirectory(Paths.get(source).toFile());
        FileUtils.deleteDirectory(Paths.get(destination).toFile());
    }

    void createFirstArchive() throws IOException {
        File first = Paths.get(source, "1.zip").toFile();
        first.createNewFile();

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(first));

        out.putNextEntry(new ZipEntry("a.txt"));
        out.write("a".getBytes());
        out.closeEntry();

        out.putNextEntry(new ZipEntry(Paths.get("f", "b.txt").toString()));
        out.write("b".getBytes());
        out.closeEntry();

        out.putNextEntry(new ZipEntry(Paths.get("f", "c.data").toString()));
        out.write("c".getBytes());
        out.closeEntry();

        out.close();
    }

    void createSecondArchive() throws IOException {
        File second = Paths.get(source, "2", "2.zip").toFile();
        second.getParentFile().mkdirs();
        second.createNewFile();

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(second));

        out.putNextEntry(new ZipEntry("d.abc"));
        out.write("d".getBytes());
        out.closeEntry();

        out.putNextEntry(new ZipEntry(Paths.get("g", "e.txt").toString()));
        out.write("e".getBytes());
        out.closeEntry();

        out.close();
    }

    @Test
    void testUnzip() {
        ReflectionTestUtils.invokeMethod(new UnzipRegex(), "unzip",
                source, destination, regex);
        assertTrue(Paths.get(destination, source, "1", "a.txt").toFile().exists());
        assertTrue(Paths.get(destination, source, "1", "f", "b.txt").toFile().exists());
        assertTrue(Paths.get(destination, source, "2", "2", "g", "e.txt").toFile().exists());
    }

    @Test
    void testFindArchives() {
        ArrayList<ZipFile> archives = new ArrayList<>();
        ReflectionTestUtils.invokeMethod(new UnzipRegex(), "findArchives",
                new File(source), archives);
        archives.sort(Comparator.comparing(ZipFile::getName));

        assertEquals(2, archives.size());
        assertEquals(Paths.get(source, "1.zip").toString(), archives.get(0).getName());
        assertEquals(Paths.get(source,"2", "2.zip").toString(), archives.get(1).getName());
    }

    @Test
    void testGetZipEntriesByRegexFirst() throws IOException {
        ZipFile first = new ZipFile(Paths.get(source, "1.zip").toString());
        ArrayList<ZipEntry> firstEntries = ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "getZipEntriesByRegex", first, regex);

        firstEntries.sort(Comparator.comparing(ZipEntry::getName));

        assertEquals(2, firstEntries.size());
        assertEquals("a.txt", firstEntries.get(0).getName());
        assertEquals(Paths.get("f", "b.txt").toString(), firstEntries.get(1).getName());
    }

    @Test
    void testGetZipEntriesByRegexSecond() throws IOException {
        ZipFile second = new ZipFile(Paths.get(source, "2", "2.zip").toString());
        ArrayList<ZipEntry> secondEntries = ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "getZipEntriesByRegex", second, regex);

        secondEntries.sort(Comparator.comparing(ZipEntry::getName));
        assertEquals(1, secondEntries.size());
        assertEquals(Paths.get("g", "e.txt").toString(), secondEntries.get(0).getName());
    }

    @Test
    void testUnzipEntryToFirst() throws IOException {
        ZipFile first = new ZipFile(Paths.get(source, "1.zip").toString());
        ArrayList<ZipEntry> firstEntries = ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "getZipEntriesByRegex", first, regex);

        Path pathA = Paths.get(destination, "a.txt");
        ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "unzipEntryTo", pathA.toString(), first.getInputStream(firstEntries.get(0)));
        assertArrayEquals("a".getBytes(), Files.readAllBytes(pathA));

        Path pathB = Paths.get(destination, "b.txt");
        ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "unzipEntryTo", pathB.toString(), first.getInputStream(firstEntries.get(1)));
        assertArrayEquals("b".getBytes(), Files.readAllBytes(pathB));
    }

    @Test
    void testUnzipEntryToSecond() throws IOException {
        ZipFile second = new ZipFile(Paths.get(source, "2", "2.zip").toString());
        ArrayList<ZipEntry> firstEntries = ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "getZipEntriesByRegex", second, regex);

        Path pathE = Paths.get(destination, "e.txt");
        ReflectionTestUtils.invokeMethod(new UnzipRegex(),
                "unzipEntryTo", pathE.toString(), second.getInputStream(firstEntries.get(0)));
        assertArrayEquals("e".getBytes(), Files.readAllBytes(pathE));
    }
}
