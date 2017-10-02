package me.eranik.test;

import me.eranik.util.Trie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {
    private Trie trie;

    @BeforeEach
    public void setUp() {
        trie = new Trie();
    }

    @Test
    public void add() {
        assertTrue(trie.add("abacaba"));
        assertTrue(trie.add("abcba"));
        assertTrue(trie.add("12345helloWorld!"));
        assertFalse(trie.add("abcba"));
    }

    @Test
    public void contains() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        assertTrue(trie.contains("abcba"));
        assertTrue(trie.contains("abacaba"));
        assertTrue(trie.contains("12345!"));
        assertFalse(trie.contains("12345"));
        assertFalse(trie.contains("hello"));
    }

    @Test
    public void remove() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        assertTrue(trie.remove("abcba"));
        assertTrue(trie.remove("12345!"));
        assertFalse(trie.remove("abcba"));
        assertFalse(trie.remove("ab"));
    }

    @Test
    public void size() {
        assertEquals(trie.size(), 0);
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        assertEquals(trie.size(), 3);
        trie.remove("abcba");
        trie.remove("def");
        assertEquals(trie.size(), 2);

    }

    @Test
    public void startsWith() {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        trie.add("aaa");
        assertEquals(trie.startsWith("ab"), 2);
        assertEquals(trie.startsWith("a"), 3);
        assertEquals(trie.startsWith(""), 4);
        assertEquals(trie.startsWith("0"), 0);
    }

    @Test
    public void serialize() throws IOException {
        trie.add("abacaba");
        trie.add("abcba");
        trie.add("12345!");
        trie.add("abcba");
        trie.add("aaa");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        trie.serialize(output);
        String[] words = output.toString().split("\n");
        Arrays.sort(words);
        assertArrayEquals(words, new String[]{"12345!", "aaa", "abacaba", "abcba"});
    }

    @Test
    public void deserialize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("hello\nworld this\nis trie".getBytes());
        trie.deserialize(input);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        trie.serialize(output);
        String[] words = output.toString().split("\n");
        Arrays.sort(words);
        assertArrayEquals(words, new String[]{"hello", "is", "this", "trie", "world"});
    }

}