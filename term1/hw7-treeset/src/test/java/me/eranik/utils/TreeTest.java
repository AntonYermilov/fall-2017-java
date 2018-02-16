package me.eranik.utils;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {
    private Tree<String> tree;
    private static Comparator<String> comparator;

    private void addAll(List<String> list) {
        for (String key : list) {
            tree.add(key);
        }
    }

    private String[] toArray() {
        String[] array = new String[tree.size()];
        Iterator<String> iterator = tree.ascendingIterator();
        for (int i = 0; i < tree.size(); i++) {
            array[i] = iterator.next();
        }
        return array;
    }

    @BeforeAll
    static void setUpComparator() {
        comparator = (s1, s2) -> {
            if (s1.length() != s2.length()) {
                return s1.length() - s2.length();
            }
            return s1.compareTo(s2);
        };
    }

    @BeforeEach
    void setUpTree() {
        tree = new Tree<>(comparator);
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, tree.size());
    }

    @Test
    void testSizeStringSetAfterAdd() {
        tree.add("abc");
        tree.add("def");
        tree.add("qwer");
        assertEquals(3, tree.size());
        tree.add("def");
        assertEquals(3, tree.size());
    }

    @Test
    void testSizeStringSetAfterRemove() {
        tree.add("abc");
        tree.add("def");
        tree.add("qwer");
        tree.remove("qwerty");
        assertEquals(3, tree.size());
        tree.remove("def");
        assertEquals(2, tree.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(tree.isEmpty());
    }

    @Test
    void testIsEmptyAfterAdd() {
        tree.add("abc");
        assertFalse(tree.isEmpty());
    }

    @Test
    void testIsEmptyAfterRemove() {
        tree.add("abc");
        tree.remove("abc");
        assertTrue(tree.isEmpty());
    }

    @Test
    void testAddStringSet() {
        tree.add("abc");
        tree.add("qwer");
        tree.add("def");
        assertArrayEquals(new String[]{"abc", "def", "qwer"}, toArray());
    }

    @Test
    void testAddEqualStringSet() {
        tree.add("abc");
        tree.add("abc");
        tree.add("qwer");
        tree.add("def");
        tree.add("qwer");
        assertArrayEquals(new String[]{"abc", "def", "qwer"}, toArray());
    }

    @Test
    void testContainsStringSetAfterAdd() {
        tree.add("abc");
        tree.add("qwer");
        assertTrue(tree.contains("abc"));
        assertFalse(tree.contains("def"));
        assertTrue(tree.contains("qwer"));
        tree.add("def");
        assertTrue(tree.contains("def"));
    }

    @Test
    void testContainsStringSetAfterRemove() {
        tree.add("abc");
        tree.add("qwer");
        tree.add("def");
        tree.remove("def");
        assertTrue(tree.contains("abc"));
        assertFalse(tree.contains("def"));
        assertTrue(tree.contains("qwer"));
        tree.remove("abc");
        assertFalse(tree.contains("abc"));
        assertTrue(tree.contains("qwer"));
    }

    @Test
    void testRemoveStringSet() {
        addAll(Arrays.asList("abc", "qwer", "xy", "def"));
        tree.remove("def");
        assertArrayEquals(new String[]{"xy", "abc", "qwer"}, toArray());
        tree.remove("abc");
        assertArrayEquals(new String[]{"xy", "qwer"}, toArray());
        tree.remove("qwer");
        assertArrayEquals(new String[]{"xy"}, toArray());
    }

    @Test
    void testRemoveEqualsStringSet() {
        addAll(Arrays.asList("qwer", "abc", "def"));
        tree.remove("qwer");
        tree.remove("qwer");
        tree.remove("qwer");
        assertArrayEquals(new String[]{"abc", "def"}, toArray());
    }

    @Test
    void testIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        addAll(list);
        list.sort(comparator);

        Iterator<String> iterator = tree.ascendingIterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        addAll(list);
        list.sort(comparator);
        Collections.reverse(list);

        Iterator<String> iterator = tree.descendingIterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFirstAscendingOrderStringSet() {
        addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("is", tree.first());
    }

    @Test
    void testLastAscendingOrderStringSet() {
        addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("leaves", tree.last());
    }

    @Test
    void testLowerAscendingOrderStringSet() {
        addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", tree.lower("HelloWorld!"));
        assertEquals("cold", tree.lower("deep"));
        assertEquals("old", tree.lower("ole"));
        assertEquals(null, tree.lower("hi"));
    }

    @Test
    void testFloorAscendingOrderStringSet() {
        addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", tree.floor("HelloWorld!"));
        assertEquals("deep", tree.floor("deep"));
        assertEquals("old", tree.floor("ole"));
        assertEquals(null, tree.floor("hi"));
    }

    @Test
    void testCeilingAscendingOrderStringSet() {
        addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", tree.ceiling("mountains"));
        assertEquals("cold", tree.ceiling("cold"));
        assertEquals("the", tree.ceiling("ole"));
        assertEquals("to", tree.ceiling("hi"));
        assertEquals(null, tree.ceiling("misty mountain"));
    }

    @Test
    void testHigherAscendingOrderStringSet() {
        addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals(null, tree.higher("mountains"));
        assertEquals("deep", tree.higher("cold"));
        assertEquals("the", tree.higher("ole"));
        assertEquals("to", tree.higher("hi"));
    }
}