package me.eranik.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetTest {
    private Set<Integer> setInteger;
    private Set<String> setString;

    @BeforeEach
    void initSets() {
        setInteger = new Set<>();
        setString = new Set<>();
    }

    @Test
    void testAddInteger() {
        assertTrue(setInteger.add(1));
        assertTrue(setInteger.add(2));
        assertTrue(setInteger.add(-3));
        assertFalse(setInteger.add(1));
        assertFalse(setInteger.add(2));
        assertFalse(setInteger.add(-3));
    }

    @Test
    void testAddString() {
        assertTrue(setString.add("abc"));
        assertTrue(setString.add("def"));
        assertTrue(setString.add("ghi"));
        assertFalse(setString.add("abc"));
        assertFalse(setString.add("def"));
        assertFalse(setString.add("ghi"));
    }

    @Test
    void testContainsEmpty() {
        assertFalse(setInteger.contains(0));
        assertFalse(setInteger.contains(5));
        assertFalse(setString.contains("abc"));
        assertFalse(setString.contains(""));
    }

    @Test
    void testContainsInteger() {
        setInteger.add(5);
        setInteger.add(0);
        assertTrue(setInteger.contains(5));
        assertTrue(setInteger.contains(0));
        assertFalse(setInteger.contains(2));
    }

    @Test
    void testContainsString() {
        setString.add("abc");
        setString.add("def");
        assertTrue(setString.contains("abc"));
        assertTrue(setString.contains("def"));
        assertFalse(setString.contains(""));
    }

    @Test
    void testSizeEmpty() {
        assertEquals(setInteger.size(), 0);
        assertEquals(setString.size(), 0);
    }

    @Test
    void testSizeInteger() {
        setInteger.add(0);
        assertEquals(setInteger.size(), 1);
        setInteger.add(5);
        assertEquals(setInteger.size(), 2);
    }

    @Test
    void testSizeString() {
        setString.add("abc");
        assertEquals(setString.size(), 1);
        setString.add("");
        assertEquals(setString.size(), 2);
    }

}