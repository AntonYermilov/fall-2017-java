package me.eranik;

import me.eranik.threads.LightFuture;
import me.eranik.threads.ThreadPool;

/**
 * Provides simple tests to thread pool.
 */
public class Main {

    public static void main(String[] args) {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        System.out.println(task.get());

        LightFuture<Integer> task1 = pool.addTask(() -> 2 * 3);
        LightFuture<Integer> task2 = task1.thenApply(i -> i + 1);
        LightFuture<Integer> task3 = task1.thenApply(i -> i * i);

        System.out.println(task1.get());
        System.out.println(task2.get());
        System.out.println(task3.get());

        pool.shutdown();
    }
}
