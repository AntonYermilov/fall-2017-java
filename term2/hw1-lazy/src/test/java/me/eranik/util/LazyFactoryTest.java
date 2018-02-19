package me.eranik.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LazyFactoryTest {

    @Test
    void testLazySimple() {
        Lazy<Integer> task = LazyFactory.createLazy(() -> 3);
        assertEquals(3, task.get().intValue());
    }

    @Test
    void testLazySameResult() {
        Lazy<Integer> task = LazyFactory.createLazy(() -> 3);
        for (int i = 0; i < 100; i++) {
            assertEquals(3, task.get().intValue());
        }
    }

    @Test
    void testLazySameResultRandom() {
        Random rand = new Random();
        Lazy<Integer> task = LazyFactory.createLazy(rand::nextInt);
        int result = task.get();
        for (int i = 0; i < 100; i++) {
            assertEquals(result, task.get().intValue());
        }
    }

    @Test
    void testLazyNullable() {
        Lazy<Integer> task = LazyFactory.createLazy(() -> null);
        for (int i = 0; i < 100; i++) {
            assertEquals(null, task.get());
        }
    }

    @Test
    void testConcurrentLazySimple() {
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(() -> 3);
        assertEquals(3, task.get().intValue());
    }

    @Test
    void testConcurrentLazySameResult() {
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(() -> 3);
        for (int i = 0; i < 100; i++) {
            assertEquals(3, task.get().intValue());
        }
    }

    @Test
    void testConcurrentLazySameResultRandom() {
        Random rand = new Random();
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(rand::nextInt);
        int result = task.get();
        for (int i = 0; i < 100; i++) {
            assertEquals(result, task.get().intValue());
        }
    }

    @Test
    void testConcurrentLazyNullable() {
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(() -> null);
        for (int i = 0; i < 100; i++) {
            assertEquals(null, task.get());
        }
    }

    @Test
    void testConcurrentLazySameResultWithManyThreads() throws InterruptedException {
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(() -> 3);

        Thread[] threads = new Thread[10];
        boolean[] correct = new boolean[10];
        for (int i = 0; i < 10; i++) {
            final int pos = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    assertEquals(3, task.get().intValue());
                }
                correct[pos] = true;
            });
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
            assertTrue(correct[i]);
        }
    }

    @Test
    void testConcurrentLazySameResultRandomWithManyThreads() throws InterruptedException {
        Random rand = new Random();
        Lazy<Integer> task = LazyFactory.createConcurrentLazy(rand::nextInt);

        Thread[] threads = new Thread[10];
        boolean[] correct = new boolean[10];
        for (int i = 0; i < 10; i++) {
            final int pos = i;
            threads[i] = new Thread(() -> {
                int result = task.get();
                for (int j = 0; j < 100; j++) {
                    assertEquals(result, task.get().intValue());
                }
                correct[pos] = true;
            });
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
            assertTrue(correct[i]);
        }
    }
}