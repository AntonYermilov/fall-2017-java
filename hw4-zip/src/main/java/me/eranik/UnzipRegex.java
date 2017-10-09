package me.eranik;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipRegex {

    private static byte[] buffer = new byte[1024];

    /**
     * Finds all zip archives in {@code source} folder and extracts
     * files that match {@code regex} to {@code destination} folder.
     * @param source a path to source folder
     * @param destination a path to destination folder
     * @param regex a regular expression to be matched
     */
    public static void unzip(@NotNull String source, @NotNull String destination, @NotNull String regex) {
        ArrayList<ZipFile> archives = new ArrayList<>();
        findArchives(new File(source), archives);

        for (ZipFile archive : archives) {
            String archiveName = archive.getName().substring(0, archive.getName().length() - 4);
            for (ZipEntry entry : getZipEntriesByRegex(archive, regex)) {
                Path path = Paths.get(destination, archiveName, entry.getName());
                try {
                    InputStream input = archive.getInputStream(entry);
                    unzipEntryTo(path.toString(), input);
                } catch (IOException e) {
                    System.err.println(path + ": " + e.getMessage());
                }
            }
        }
    }

    private static void findArchives(File file, ArrayList<ZipFile> archives) {
        if (!file.exists())
            return ;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null)
                return;
            for (File child : children) {
                findArchives(child, archives);
            }
        } else if (file.getName().endsWith(".zip")) {
            try {
                archives.add(new ZipFile(file.toString()));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static ArrayList<ZipEntry> getZipEntriesByRegex(ZipFile zip, String regex) {
        ArrayList<ZipEntry> entries = new ArrayList<>();
        Enumeration<? extends ZipEntry> enumerator = zip.entries();
        while (enumerator.hasMoreElements()) {
            ZipEntry entry = enumerator.nextElement();
            if (entry.isDirectory())
                continue;
            if (entry.toString().matches(regex)) {
                entries.add(entry);
            }
        }
        return entries;
    }

    private static void unzipEntryTo(String destination, InputStream zipInput) {
        try {
            File file = new File(destination);
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                System.err.println("File " + destination + " already exists");
                return;
            }

            FileOutputStream output = new FileOutputStream(file);
            while (true) {
                int len = zipInput.read(buffer);
                if (len <= 0)
                    break;
                output.write(buffer, 0, len);
            }
            output.close();
        } catch (IOException e) {
            System.out.println(destination + ": " + e.getMessage());
        }
    }

}
