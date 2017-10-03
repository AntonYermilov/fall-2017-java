package me.eranik.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    @Test
    void testAdd() {
        assertTrue(trie.add("abacaba"));
        assertTrue(trie.add("abcba"));
        assertTrue(trie.add("12345helloWorld!"));
        assertTrue(trie.add(""));
        assertFalse(trie.add("abcba"));
    }

    @Test
    void testContains() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("");
        assertTrue(trie.contains("abcba"));
        assertTrue(trie.contains("abacaba"));
        assertTrue(trie.contains("12345!"));
        assertTrue(trie.contains(""));
        assertFalse(trie.contains("12345"));
        assertFalse(trie.contains("hello"));
    }

    @Test
    void testRemove() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("");
        assertTrue(trie.remove("abcba"));
        assertTrue(trie.remove("12345!"));
        assertTrue(trie.remove(""));
        assertFalse(trie.remove("abcba"));
        assertFalse(trie.remove("ab"));
    }

    @Test
    void testSize() {
        assertEquals(trie.size(), 0);
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        trie.add("");
        assertEquals(trie.size(), 4);
        trie.remove("abcba");
        trie.remove("def");
        assertEquals(trie.size(), 3);
    }

    @Test
    void testStartsWith() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        trie.add("aaa");
        trie.add("");
        assertEquals(trie.startsWith("ab"), 2);
        assertEquals(trie.startsWith("a"), 3);
        assertEquals(trie.startsWith(""), 5);
        assertEquals(trie.startsWith("0"), 0);
    }

    @Test
    void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        trie.add("aaa");
        trie.add("");


        ByteArrayOutputStream output = new ByteArrayOutputStream();
        trie.serialize(output);

        trie = new Trie();
        assertEquals(trie.size(), 0);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        trie.deserialize(input);

        assertEquals(trie.size(), 5);
        assertTrue(trie.contains("abacaba"));
        assertTrue(trie.contains("abcba"));
        assertTrue(trie.contains("12345!"));
        assertTrue(trie.contains("aaa"));
        assertTrue(trie.contains(""));
    }

}