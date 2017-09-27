package me.eranik.test;

import me.eranik.algorithm.HashTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {
    HashTable table;

    @BeforeEach
    public void initTable() {
        table = new HashTable();
    }

    @Test
    public void testSizeEmptyHashTable() {
        assertEquals(table.size(), 0);
    }

    @Test
    public void testSizeHashTableAfterPut() {
        table.put("abc", "5");
        assertEquals(table.size(), 1);
        table.put("abc", "6");
        assertEquals(table.size(), 1);
        table.put("def", "7");
        assertEquals(table.size(), 2);
        table.put("ghi", "7");
        assertEquals(table.size(), 3);
    }

    @Test
    public void testSizeHashTableAfterRemove() {
        table.put("abc", "5");
        table.put("abc", "6");
        table.put("def", "6");
        table.put("ghi", "7");
        table.remove("abc");
        assertEquals(table.size(), 2);
        table.remove("def");
        assertEquals(table.size(), 1);
        table.remove("def");
        assertEquals(table.size(), 1);
    }

    @Test
    public void testSizeHashTableAfterClear() {
        table.put("abc", "1");
        table.put("def", "2");
        table.clear();
        assertEquals(table.size(), 0);
        table.clear();
        assertEquals(table.size(), 0);
    }

    @Test
    public void testContainsInEmptyHashTable() {
        assertFalse(table.contains("abc"));
        assertFalse(table.contains(""));
    }

    @Test
    public void testContainsInHashTableAfterPut() {
        table.put("abc", "1");
        table.put("def", "2");
        assertTrue(table.contains("abc"));
        assertTrue(table.contains("def"));
        assertFalse(table.contains("ghi"));
    }

    @Test
    public void testContainsInHashTableAfterRemove() {
        table.put("abc", "1");
        table.put("def", "2");
        table.put("ghi", "3");
        table.remove("abc");
        assertTrue(table.contains("def"));
        assertTrue(table.contains("ghi"));
        assertFalse(table.contains("abc"));
        table.remove("abc");
        assertFalse(table.contains("abc"));
    }

    @Test
    public void testContainsHashTableAfterClear() {
        table.put("abc", "1");
        table.put("def", "2");
        table.clear();
        assertFalse(table.contains("abc"));
        assertFalse(table.contains("def"));
    }

    @Test
    public void testGet() {
        table.put("abc", "1");
        table.put("def", "2");
        assertEquals(table.get("abc"), "1");
        assertEquals(table.get("def"), "2");
        assertNull(table.get("ghi"));
        table.put("abc", "2");
        assertEquals(table.get("abc"), "2");
        table.remove("abc");
        assertNull(table.get("abc"));
    }

    @Test
    public void testPutFewElements() {
        table.put("abc", "1");
        assertEquals(table.size(), 1);
        assertTrue(table.contains("abc"));
        assertEquals(table.get("abc"), "1");
        table.put("def", "2");
        assertEquals(table.size(), 2);
        assertTrue(table.contains("def"));
        assertEquals(table.get("def"), "2");
        table.put("abc", "3");
        assertEquals(table.size(), 2);
        assertTrue(table.contains("abc"));
        assertEquals(table.get("abc"), "3");
    }

    @Test
    public void testPutManyElements() {
        for (int i = 0; i < 1000; i++) {
            table.put(String.valueOf(i), String.valueOf(1000 + i));
        }

        assertEquals(table.size(), 1000);
        for (int i = 0; i < 1000; i++) {
            assertTrue(table.contains(String.valueOf(i)));
            assertEquals(table.get(String.valueOf(i)), String.valueOf(1000 + i));
        }

        for (int i = 0; i < 1000; i++) {
            table.put(String.valueOf(i), String.valueOf(2000 + i));
        }

        assertEquals(table.size(), 1000);
        for (int i = 0; i < 1000; i++) {
            assertTrue(table.contains(String.valueOf(i)));
            assertEquals(table.get(String.valueOf(i)), String.valueOf(2000 + i));
        }
    }

    @Test
    public void testRemoveFewElements() {
        table.put("abc", "1");
        table.put("def", "2");
        table.put("ghi", "3");
        table.remove("jkl");
        assertEquals(table.size(), 3);
        table.remove("abc");
        assertEquals(table.size(), 2);
        assertFalse(table.contains("abc"));
        assertTrue(table.contains("def"));
        assertTrue(table.contains("ghi"));
        table.remove("def");
        assertEquals(table.size(), 1);
        assertFalse(table.contains("abc"));
        assertFalse(table.contains("def"));
        assertTrue(table.contains("ghi"));
        assertEquals(table.get("abc"), null);
        assertEquals(table.get("def"), null);
        assertEquals(table.get("ghi"), "3");
    }

    @Test
    public void testRemoveManyElements() {
        for (int i = 0; i < 1000; i++) {
            table.put(String.valueOf(i), String.valueOf(2 * i));
        }
        for (int i = 0; i < 500; i++) {
            table.remove(String.valueOf(2 * i));
        }

        assertEquals(table.size(), 500);
        for (int i = 0; i < 500; i++) {
            assertFalse(table.contains(String.valueOf(2 * i)));
            assertEquals(table.get(String.valueOf(2 * i)), null);
            assertTrue(table.contains(String.valueOf(2 * i + 1)));
            assertEquals(table.get(String.valueOf(2 * i + 1)), String.valueOf(4 * i + 2));
        }
    }

    @Test
    public void testClear() {
        table.put("abc", "1");
        table.put("def", "2");
        table.clear();
        assertEquals(table.size(), 0);
        assertEquals(table.get("abc"), null);
        assertEquals(table.get("def"), null);
    }

}