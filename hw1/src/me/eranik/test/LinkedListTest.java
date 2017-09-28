package me.eranik.test;

import me.eranik.algorithm.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {
    public LinkedList list;

    @BeforeEach
    public void initLinkedList() {
        list = new LinkedList();
    }

    @Test
    public void add() {
        list.add("1");
        list.add("2");
        list.add("3");
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "2");
        assertEquals(list.get(2), "3");
    }

    @Test
    public void get() {
        assertEquals(list.get(0), null);
        list.add("1");
        list.add("2");
        list.add("3");
        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "2");
        assertEquals(list.get(2), "3");
        assertEquals(list.get(3), null);
    }

    @Test
    public void set() {
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5; i++) {
            list.set(i, 10 + i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), 10 + i);
        }
    }

    @Test
    public void remove() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5; i++) {
            list.remove(i + 1);
        }

        assertEquals(list.size(), 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), 2 * i);
        }
    }

    @Test
    public void indexOf() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < 10; i++) {
            list.add(2 * i);
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(list.indexOf(i), i);
        }
        for (int i = 10; i < 20; i += 2) {
            assertEquals(list.indexOf(i), 10 + i / 2);
        }
        for (int i = 11; i < 20; i += 2) {
            assertEquals(list.indexOf(i), -1);
        }
    }

    @Test
    public void contains() {
        for (int i = 0; i < 10; i++) {
            list.add(2 * i);
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(list.contains(2 * i));
            assertFalse(list.contains(2 * i + 1));
        }
    }

    @Test
    public void isEmpty() {
        assertTrue(list.isEmpty());
        list.add(1);
        assertFalse(list.isEmpty());
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    public void size() {
        assertEquals(list.size(), 0);
        for (int i = 0; i < 500; i++) {
            list.add(i);
            assertEquals(list.size(), i + 1);
        }
        for (int i = 0; i < 250; i++) {
            list.remove(0);
            assertEquals(list.size(), 499 - i);
        }
        list.clear();
        assertEquals(list.size(), 0);
    }

    @Test
    public void clear() {
        for (int i = 0; i < 10; i++) {
            list.add("abc");
        }
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    public void toArray() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        Object[] array = list.toArray();
        for (int i = 0; i < 10; i++) {
            assertEquals((int)(array[i]), i);
        }
    }

}