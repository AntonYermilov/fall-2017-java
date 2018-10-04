package me.eranik.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Provides possibility to calculate MD5 sum of the specified directory either in the same thread
 * or using fork-join pool.
 */
public class MD5Calculator {

    private static final int BUFFER_SIZE = 4096;

    private boolean multithread;
    private ForkJoinPool pool = null;

    /**
     * Creates MD5 calculator.
     * @param multithread {@code true} if you want to create calculator based on fork-join pool; {@code false} otherwise
     */
    public MD5Calculator(boolean multithread) {
        this.multithread = multithread;
        if (multithread) {
            pool = new ForkJoinPool();
        }
    }

    /**
     * Counts MD5 sum of the specified file or directory.
     * Uses recursive algorithm that counts MD5 sum of the specified directory name and then appends
     * to it MD5 sums of its subdirectories in increasing alphabet order
     * @param file specified file to count MD5 sum of
     * @return byte array that contains MD5 sum of the specified file or directory
     */
    public byte[] countMD5(File file) {
        if (file.isFile()) {
            try (DigestInputStream in = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                for (int read = in.read(buffer); read != -1; read = in.read(buffer));
                return in.getMessageDigest().digest();
            } catch (IOException e) {
                System.err.println("Some error occurred while reading file");
                System.err.println(e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Algorithm MD5 is not supported");
            }
        }
        if (file.isDirectory()){
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(file.getName().getBytes());

                File[] files = file.listFiles();
                Arrays.sort(files, Comparator.comparing(File::getName));

                byte[][] md5s = new byte[files.length + 1][];
                md5s[0] = digest.digest();

                if (multithread) {
                    ArrayList<ForkJoinTask> tasks = new ArrayList<>();
                    for (int i = 0; i < files.length; i++) {
                        final int j = i;
                        tasks.add(pool.submit(() -> md5s[j + 1] = countMD5(files[j])));
                    }
                    for (ForkJoinTask task : tasks) {
                        task.join();
                    }
                } else {
                    for (int i = 0; i < files.length; i++) {
                        md5s[i + 1] = countMD5(files[i]);
                    }
                }
                return merge(md5s);
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Algorithm MD5 is not supported");
            }
        }
        return new byte[0];
    }

    /**
     * Merges arrays of MD5 sums into one array.
     * @param md5s array of MD5 sums
     * @return merged array of MD5 sums
     */
    private static byte[] merge(byte[][] md5s) {
        int length = 0;
        for (byte[] instance : md5s) {
            length += instance.length;
        }

        int copied = 0;
        byte[] result = new byte[length];
        for (byte[] instance : md5s) {
            System.arraycopy(instance, 0, result, copied, instance.length);
            copied += instance.length;
        }
        return result;
    }

}
