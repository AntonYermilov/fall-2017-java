package me.eranik;

import me.eranik.util.Lazy;
import me.eranik.util.LazyFactory;

public class Main {

    public static void main(String[] args) {
        Lazy<Long> task = LazyFactory.createLazy(() -> {
            long sum = 0;
            try {
                Thread.sleep(2000);
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    sum += i;
                }
            } catch (InterruptedException ignored) {
            }
            return sum;
        });
        System.out.println("Waiting for task to begin");

        System.out.println(task.get());
        System.out.println(task.get());
    }
}
