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
        assertEquals(list.get(-1), null);
        assertEquals(list.get(0), null);
        list.add("1");
        list.add("2");
        list.add("3");
        assertEquals(list.get(0), "1");
        assertEquals(list.get(2), "2");
        assertEquals(list.get(3), "3");
        assertEquals(list.get(4), null);
    }

    @Test
    public void set() {
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        for (int i = -1; i < 6; i++) {
            list.set(i, 10 + i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), 10 + i);
        }
    }

    @Test
    public void remove() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void isEmpty() {

    }

    @Test
    public void size() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void toArray() {
    }

}