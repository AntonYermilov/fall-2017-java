package me.eranik;

import java.io.File;
import java.util.ArrayList;

public class UnzipRegex {
    public static void main(String[] args) {
        ArrayList<File> archives = new ArrayList<>();
        findArchives(new File(args[0]), archives);
    }

    private static void findArchives(File file, ArrayList<File> archives) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null)
                return;
            for (File child : children) {
                findArchives(child, archives);
            }
        } else if (file.getName().endsWith(".zip")) {
            archives.add(file);
        }
    }
}
