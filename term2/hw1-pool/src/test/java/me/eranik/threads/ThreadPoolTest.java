package me.eranik.threads;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Supplier;

class ThreadPoolTest {

    @Test
    void testTasksWithOneThread() {
        ThreadPool<String> pool = new ThreadPool<>(1);
        LightFuture<String> task1 = pool.addTask(() -> "Hello world!");
        LightFuture<String> task2 = pool.addTask(() -> "Hello world!".substring(6));
        LightFuture<String> task3 = pool.addTask(() -> Integer.toString(100500));
        assertEquals("Hello world!", task1.get());
        assertEquals("world!", task2.get());
        assertEquals("100500", task3.get());

        System.out.println("testTasksWithOneThread: Successful");
        System.out.flush();
    }

    @Test
    void testTasksWithTwoThreads() {
        ThreadPool<String> pool = new ThreadPool<>(2);
        LightFuture<String> task1 = pool.addTask(() -> "Hello world!");
        LightFuture<String> task2 = pool.addTask(() -> "Hello world!".substring(6));
        LightFuture<String> task3 = pool.addTask(() -> Integer.toString(100500));
        assertEquals("Hello world!", task1.get());
        assertEquals("world!", task2.get());
        assertEquals("100500", task3.get());

        System.out.println("testTasksWithTwoThreads: Successful");
        System.out.flush();
    }

    @Test
    void testTasksWithManyThreads() {
        ThreadPool<String> pool = new ThreadPool<>(10);
        LightFuture<String> task1 = pool.addTask(() -> "Hello world!");
        LightFuture<String> task2 = pool.addTask(() -> "Hello world!".substring(6));
        LightFuture<String> task3 = pool.addTask(() -> Integer.toString(100500));
        assertEquals("Hello world!", task1.get());
        assertEquals("world!", task2.get());
        assertEquals("100500", task3.get());

        System.out.println("testTasksWithManyThreads: Successful");
        System.out.flush();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testManyTasksWithOneThread() {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        LightFuture<Integer>[] tasks = new LightFuture[1000];
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            tasks[i] = pool.addTask(() -> j * j);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * i, tasks[i].get().intValue());
        }

        System.out.println("testManyTasksWithOneThread: Successful");
        System.out.flush();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testManyTasksWithManyThreads() {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer>[] tasks = new LightFuture[1000];
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            tasks[i] = pool.addTask(() -> j * j);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * i, tasks[i].get().intValue());
        }

        System.out.println("testManyTasksWithManyThreads: Successful");
        System.out.flush();
    }

    @Test
    void testShutdown() throws InterruptedException {
        Supplier<String> job = () -> {
            while (!Thread.interrupted()) {
                Thread.yield();
            }
            return "I have finished!";
        };

        ThreadPool<String> pool = new ThreadPool<>(2);
        LightFuture<String> task1 = pool.addTask(job);
        LightFuture<String> task2 = pool.addTask(job);
        LightFuture<String> task3 = pool.addTask(job);
        LightFuture<String> task4 = pool.addTask(job);

        assertFalse(task1.isReady());
        assertFalse(task2.isReady());
        assertFalse(task3.isReady());
        assertFalse(task4.isReady());

        pool.shutdown();
        Thread.sleep(10000);

        assertTrue(task1.isReady());
        assertTrue(task2.isReady());
        assertFalse(task3.isReady());
        assertFalse(task4.isReady());

        System.out.println("testShutdown: Successful");
        System.out.flush();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testThenApply() {
        ThreadPool<Integer> pool = new ThreadPool<>(4);

        LightFuture<Integer>[] tasks = new LightFuture[7];
        tasks[0] = pool.addTask(() -> 1);
        for (int i = 1; i < 7; i++) {
            final int j = i;
            tasks[i] = tasks[(i - 1) / 2].thenApply(k -> k * 2 + (j + 1) % 2);
        }
        for (int i = 0; i < 7; i++) {
            assertEquals(i + 1, tasks[i].get().intValue());
        }
        System.out.println("testThenApply: Successful");
        System.out.flush();
    }

    @Test
    void testMultipleThreads() throws InterruptedException {
        ThreadPool<Integer> pool = new ThreadPool<>(2);

        Thread[] threads = new Thread[10];
        boolean[] correct = new boolean[10];
        for (int i = 0; i < 10; i++) {
            final int num = i;
            threads[i] = new Thread(() -> {
                Random random = new Random();
                final int j = random.nextInt();
                final int k = random.nextInt();
                LightFuture<Integer> task1 = pool.addTask(() -> j * j);
                LightFuture<Integer> task2 = pool.addTask(() -> j * k);
                assertEquals(j * j, task1.get().intValue());
                assertEquals(j * k, task2.get().intValue());
                correct[num] = true;
            });
            threads[i].setDaemon(true);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
            assertTrue(correct[i]);
        }

        System.out.println("testMultipleThreads: Successful");
        System.out.flush();
    }
}