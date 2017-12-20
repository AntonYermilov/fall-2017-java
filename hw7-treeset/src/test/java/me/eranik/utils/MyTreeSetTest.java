package me.eranik.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyTreeSetTest {
    private MyTreeSet<Integer> intSet;
    private MyTreeSet<String> strSet;
    private static Comparator<String> comparator;

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
    void setUpSets() {
        intSet = new MyTreeSet<>();
        strSet = new MyTreeSet<>(comparator);
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, intSet.size());
        assertEquals(0, strSet.size());
    }

    @Test
    void testSizeIntegerSetAfterAdd() {
        intSet.add(1);
        intSet.add(3);
        assertEquals(2, intSet.size());
        intSet.add(3);
        assertEquals(2, intSet.size());
    }

    @Test
    void testSizeIntegerSetAfterRemove() {
        intSet.add(1);
        intSet.add(3);
        intSet.remove(2);
        assertEquals(2, intSet.size());
        intSet.remove(3);
        assertEquals(1, intSet.size());
    }

    @Test
    void testSizeStringSetAfterAdd() {
        strSet.add("abc");
        strSet.add("def");
        strSet.add("qwer");
        assertEquals(3, strSet.size());
        strSet.add("def");
        assertEquals(3, strSet.size());
    }

    @Test
    void testSizeStringSetAfterRemove() {
        strSet.add("abc");
        strSet.add("def");
        strSet.add("qwer");
        strSet.remove("qwerty");
        assertEquals(3, strSet.size());
        strSet.remove("def");
        assertEquals(2, strSet.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(intSet.isEmpty());
        assertTrue(strSet.isEmpty());
    }

    @Test
    void testIsEmptyAfterAdd() {
        intSet.add(1);
        strSet.add("abc");
        assertFalse(intSet.isEmpty());
        assertFalse(strSet.isEmpty());
    }

    @Test
    void testIsEmptyAfterRemove() {
        intSet.add(1);
        strSet.add("abc");
        intSet.remove(1);
        strSet.remove("abc");
        assertTrue(intSet.isEmpty());
        assertTrue(strSet.isEmpty());
    }

    @Test
    void testAddIntegerSet() {
        intSet.add(1);
        intSet.add(3);
        intSet.add(2);
        assertEquals("[1, 2, 3]", intSet.toString());
    }

    @Test
    void testAddEqualIntegerSet() {
        intSet.add(1);
        intSet.add(3);
        intSet.add(2);
        intSet.add(3);
        intSet.add(2);
        intSet.add(1);
        assertEquals("[1, 2, 3]", intSet.toString());
    }

    @Test
    void testAddStringSet() {
        strSet.add("abc");
        strSet.add("qwer");
        strSet.add("def");
        assertEquals("[abc, def, qwer]", strSet.toString());
    }

    @Test
    void testAddEqualStringSet() {
        strSet.add("abc");
        strSet.add("abc");
        strSet.add("qwer");
        strSet.add("def");
        strSet.add("qwer");
        assertEquals("[abc, def, qwer]", strSet.toString());
    }

    @Test
    void testContainsIntegerSetAfterAdd() {
        intSet.add(1);
        intSet.add(3);
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(3));
        assertFalse(intSet.contains(2));
        intSet.add(2);
        assertTrue(intSet.contains(2));
    }

    @Test
    void testContainsStringSetAfterAdd() {
        strSet.add("abc");
        strSet.add("qwer");
        assertTrue(strSet.contains("abc"));
        assertFalse(strSet.contains("def"));
        assertTrue(strSet.contains("qwer"));
        strSet.add("def");
        assertTrue(strSet.contains("def"));
    }

    @Test
    void testContainsIntegerSetAfterRemove() {
        intSet.add(1);
        intSet.add(2);
        intSet.add(3);
        intSet.remove(2);
        assertTrue(intSet.contains(1));
        assertFalse(intSet.contains(2));
        assertTrue(intSet.contains(3));
        intSet.remove(3);
        assertTrue(intSet.contains(1));
        assertFalse(intSet.contains(3));
    }

    @Test
    void testContainsStringSetAfterRemove() {
        strSet.add("abc");
        strSet.add("qwer");
        strSet.add("def");
        strSet.remove("def");
        assertTrue(strSet.contains("abc"));
        assertFalse(strSet.contains("def"));
        assertTrue(strSet.contains("qwer"));
        strSet.remove("abc");
        assertFalse(strSet.contains("abc"));
        assertTrue(strSet.contains("qwer"));
    }

    @Test
    void testRemoveIntegerSet() {
        intSet.addAll(Arrays.asList(2, 1, 4, 3));
        intSet.remove(2);
        assertArrayEquals(new Integer[]{1, 3, 4}, intSet.toArray());
        intSet.remove(4);
        assertArrayEquals(new Integer[]{1, 3}, intSet.toArray());
        intSet.remove(3);
        assertArrayEquals(new Integer[]{1}, intSet.toArray());
    }

    @Test
    void testRemoveStringSet() {
        strSet.addAll(Arrays.asList("abc", "qwer", "xy", "def"));
        strSet.remove("def");
        assertArrayEquals(new String[]{"xy", "abc", "qwer"}, strSet.toArray());
        strSet.remove("abc");
        assertArrayEquals(new String[]{"xy", "qwer"}, strSet.toArray());
        strSet.remove("qwer");
        assertArrayEquals(new String[]{"xy"}, strSet.toArray());
    }

    @Test
    void testRemoveEqualsIntegerSet() {
        intSet.addAll(Arrays.asList(3, 1, 2));
        intSet.remove(2);
        intSet.remove(2);
        intSet.remove(2);
        assertArrayEquals(new Integer[]{1, 3}, intSet.toArray());
    }

    @Test
    void testRemoveEqualsStringSet() {
        strSet.addAll(Arrays.asList("qwer", "abc", "def"));
        strSet.remove("qwer");
        strSet.remove("qwer");
        strSet.remove("qwer");
        assertArrayEquals(new String[]{"abc", "def"}, strSet.toArray());
    }

    @Test
    void testIteratorIntegerSet() {
        intSet.addAll(Arrays.asList(3, 1, 5, 4, 2));
        Iterator<Integer> iterator = intSet.iterator();
        for (int i = 1; i <= 5; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        strSet.addAll(list);
        list.sort(comparator);

        Iterator<String> iterator = strSet.iterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingIteratorIntegerSet() {
        intSet.addAll(Arrays.asList(3, 1, 5, 4, 2));
        Iterator<Integer> iterator = intSet.descendingIterator();
        for (int i = 5; i >= 1; i--) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        strSet.addAll(list);
        list.sort(comparator);
        Collections.reverse(list);

        Iterator<String> iterator = strSet.descendingIterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingSetIteratorIntegerSet() {
        intSet.addAll(Arrays.asList(5, 3, 1, 4, 2));
        MyTreeSet<Integer> descIntSet = (MyTreeSet<Integer>) intSet.descendingSet();

        Iterator<Integer> iterator = descIntSet.iterator();
        for (int i = 5; i >= 1; i--) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingSetDescendingIteratorIntegerSet() {
        intSet.addAll(Arrays.asList(5, 3, 1, 4, 2));
        MyTreeSet<Integer> descIntSet = (MyTreeSet<Integer>) intSet.descendingSet();

        Iterator<Integer> iterator = descIntSet.descendingIterator();
        for (int i = 1; i <= 5; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingDescendingSetIteratorIntegerSet() {
        intSet.addAll(Arrays.asList(5, 3, 1, 4, 2));
        MyTreeSet<Integer> descDescIntSet = (MyTreeSet<Integer>) intSet.descendingSet().descendingSet();

        Iterator<Integer> iterator = descDescIntSet.iterator();
        for (int i = 1; i <= 5; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingSetIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        strSet.addAll(list);
        list.sort(comparator);
        Collections.reverse(list);

        MyTreeSet<String> descStrSet = (MyTreeSet<String>) strSet.descendingSet();
        Iterator<String> iterator = descStrSet.iterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingSetDescendingIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        strSet.addAll(list);
        list.sort(comparator);

        MyTreeSet<String> descStrSet = (MyTreeSet<String>) strSet.descendingSet();
        Iterator<String> iterator = descStrSet.descendingIterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingDescendingSetIteratorStringSet() {
        List<String> list = Arrays.asList("we", "all", "live", "in", "a", "yellow", "submarine");
        strSet.addAll(list);
        list.sort(comparator);

        MyTreeSet<String> descStrSet = (MyTreeSet<String>) strSet.descendingSet().descendingSet();
        Iterator<String> iterator = descStrSet.iterator();
        for (String str : list) {
            assertTrue(iterator.hasNext());
            assertEquals(str, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFirstAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 2, 1, 4));
        assertEquals(1, intSet.first().intValue());
    }

    @Test
    void testFirstDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 1, 2, 4));
        assertEquals(4, intSet.descendingSet().first().intValue());
    }

    @Test
    void testFirstAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("is", strSet.first());
    }

    @Test
    void testFirstDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("leaves", strSet.descendingSet().first());
    }

    @Test
    void testLastAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 2, 1, 4));
        assertEquals(4, intSet.last().intValue());
    }

    @Test
    void testLastDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 1, 2, 4));
        assertEquals(1, intSet.descendingSet().last().intValue());
    }

    @Test
    void testLastAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("leaves", strSet.last());
    }

    @Test
    void testLastDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("All", "the", "leaves", "are", "brown", "and",
                "the", "sky", "is", "gray"));
        assertEquals("is", strSet.descendingSet().last());
    }

    @Test
    void testLowerAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(6, intSet.lower(7).intValue());
        assertEquals(4, intSet.lower(5).intValue());
        assertEquals(null, intSet.lower(1));
    }

    @Test
    void testLowerDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(8, intSet.descendingSet().lower(7).intValue());
        assertEquals(6, intSet.descendingSet().lower(5).intValue());
        assertEquals(null, intSet.descendingSet().lower(10));
    }

    @Test
    void testLowerAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.lower("HelloWorld!"));
        assertEquals("cold", strSet.lower("deep"));
        assertEquals("old", strSet.lower("ole"));
        assertEquals(null, strSet.lower("hi"));
    }

    @Test
    void testLowerDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals(null, strSet.descendingSet().lower("mountains"));
        assertEquals("deep", strSet.descendingSet().lower("cold"));
        assertEquals("the", strSet.descendingSet().lower("ole"));
        assertEquals("to", strSet.descendingSet().lower("hi"));
    }

    @Test
    void testFloorAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(7, intSet.floor(7).intValue());
        assertEquals(4, intSet.floor(5).intValue());
        assertEquals(1, intSet.floor(1).intValue());
        assertEquals(null, intSet.floor(0));
    }

    @Test
    void testFloorDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(7, intSet.descendingSet().floor(7).intValue());
        assertEquals(6, intSet.descendingSet().floor(5).intValue());
        assertEquals(1, intSet.descendingSet().floor(0).intValue());
        assertEquals(null, intSet.descendingSet().floor(11));
    }

    @Test
    void testFloorAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.floor("HelloWorld!"));
        assertEquals("deep", strSet.floor("deep"));
        assertEquals("old", strSet.floor("ole"));
        assertEquals(null, strSet.floor("hi"));
    }

    @Test
    void testFloorDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.descendingSet().floor("mountains"));
        assertEquals("cold", strSet.descendingSet().floor("cold"));
        assertEquals("the", strSet.descendingSet().floor("ole"));
        assertEquals("to", strSet.descendingSet().floor("hi"));
        assertEquals(null, strSet.descendingSet().floor("misty mountain"));
    }

    @Test
    void testCeilingAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(7, intSet.ceiling(7).intValue());
        assertEquals(6, intSet.ceiling(5).intValue());
        assertEquals(1, intSet.ceiling(0).intValue());
        assertEquals(null, intSet.ceiling(11));
    }

    @Test
    void testCeilingDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(7, intSet.descendingSet().ceiling(7).intValue());
        assertEquals(4, intSet.descendingSet().ceiling(5).intValue());
        assertEquals(1, intSet.descendingSet().ceiling(1).intValue());
        assertEquals(null, intSet.descendingSet().ceiling(0));
    }

    @Test
    void testCeilingAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.ceiling("mountains"));
        assertEquals("cold", strSet.ceiling("cold"));
        assertEquals("the", strSet.ceiling("ole"));
        assertEquals("to", strSet.ceiling("hi"));
        assertEquals(null, strSet.ceiling("misty mountain"));
    }

    @Test
    void testCeilingDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.descendingSet().ceiling("HelloWorld!"));
        assertEquals("deep", strSet.descendingSet().ceiling("deep"));
        assertEquals("old", strSet.descendingSet().ceiling("ole"));
        assertEquals(null, strSet.descendingSet().ceiling("hi"));
    }

    @Test
    void testHigherAscendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(8, intSet.higher(7).intValue());
        assertEquals(6, intSet.higher(5).intValue());
        assertEquals(null, intSet.higher(10));
    }

    @Test
    void testHigherDescendingOrderIntegerSet() {
        intSet.addAll(Arrays.asList(3, 9, 8, 4, 7, 2, 6, 1, 10));
        assertEquals(6, intSet.descendingSet().higher(7).intValue());
        assertEquals(4, intSet.descendingSet().higher(5).intValue());
        assertEquals(null, intSet.descendingSet().higher(1));
    }

    @Test
    void testHigherAscendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals(null, strSet.higher("mountains"));
        assertEquals("deep", strSet.higher("cold"));
        assertEquals("the", strSet.higher("ole"));
        assertEquals("to", strSet.higher("hi"));
    }

    @Test
    void testHigherDescendingOrderStringSet() {
        strSet.addAll(Arrays.asList("Far", "over", "the", "misty", "mountains", "cold",
                "to", "dungeons", "deep", "and", "caverns", "old"));
        assertEquals("mountains", strSet.descendingSet().higher("HelloWorld!"));
        assertEquals("cold", strSet.descendingSet().higher("deep"));
        assertEquals("old", strSet.descendingSet().higher("ole"));
        assertEquals(null, strSet.descendingSet().higher("hi"));
    }

}