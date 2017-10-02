package me.eranik;

import me.eranik.util.Trie;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        testTrie();
    }

    public static void testTrie() {
        Trie trie = new Trie();

        try {
            trie.deserialize(System.in);
        } catch (IOException e) {
            System.out.println("Error while reading data");
        }

        System.out.println("add abacaba: " + trie.add("abacaba"));
        System.out.println("add 12345: " + trie.add("12345"));
        System.out.println("add abcba: " + trie.add("abcba"));
        System.out.println("size: " + trie.size());
        System.out.println("starts with ab: " + trie.startsWith("ab"));
        System.out.println("remove abacaba: " + trie.remove("abacaba"));
        System.out.println("remove abe: " + trie.remove("abe"));
        System.out.println("starts with ab: " + trie.startsWith("ab"));
        System.out.println("contains ab: " + trie.contains("ab"));
        System.out.println("contains abcba: " + trie.contains("abcba"));

        try {
            trie.serialize(System.out);
        } catch (IOException e) {
            System.out.println("Error while writing data");
        }
    }
}
