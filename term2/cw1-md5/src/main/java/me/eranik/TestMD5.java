package me.eranik;

import me.eranik.md5.MD5Calculator;

import java.io.File;

public class TestMD5 {

    /**
     * Compares the speed of calculating MD5 sum with one thread and with fork-join pool.
     * @param args list of arguments: first argument contains the name of directory to count MD5 sum.
     */
    public static void main(String[] args) {
        File dir = new File(args[0]);

        MD5Calculator calculator;
        long time;

        time = System.currentTimeMillis();
        calculator = new MD5Calculator(false);
        calculator.countMD5(dir);
        time = System.currentTimeMillis() - time;
        System.out.println("MD5 is calculated in " + time + "ms with 1 thread");

        time = System.currentTimeMillis();
        calculator = new MD5Calculator(true);
        calculator.countMD5(dir);
        time = System.currentTimeMillis() - time;
        System.out.println("MD5 is calculated in " + time + "ms with fork-join pool");
    }
}
