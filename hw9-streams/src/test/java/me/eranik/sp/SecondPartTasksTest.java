package me.eranik.sp;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> actual = SecondPartTasks.findQuotes(Arrays.asList(
                "src/test/resources/a.txt",
                "src/test/resources/b.txt",
                "src/test/resources/c.txt"),
                "cat");
        List<String> expected = Arrays.asList("cats", "cattles", "catching", "cats.",
                "cats", "categories?");

        Collections.sort(actual);
        Collections.sort(expected);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(Math.abs(SecondPartTasks.piDividedBy4() - Math.PI / 4.0) < 1e-3);
    }

    @Test
    public void testFindPrinter() {
        TreeMap<String, List<String>> map = new TreeMap<>();
        map.put("a", Arrays.asList("hello world!", "1", "2", "3"));
        map.put("b", Arrays.asList("abacaba", "aba", "a"));
        map.put("c", Arrays.asList("we", "all", "live", "in", "the", "yellow", "submarine"));
        map.put("d", Arrays.asList("abracadabra", "kazak", "pop"));
        assertEquals("c", SecondPartTasks.findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        ArrayList<Map<String, Integer>> orders = new ArrayList<>();

        TreeMap<String, Integer> c1 = new TreeMap<>();
        c1.put("a", 5);
        c1.put("b", 3);
        c1.put("c", 4);

        TreeMap<String, Integer> c2 = new TreeMap<>();
        c2.put("b", 4);
        c2.put("d", 2);

        TreeMap<String, Integer> c3 = new TreeMap<>();
        c3.put("c", 5);
        c3.put("b", 1);
        c3.put("e", 4);

        orders.addAll(Arrays.asList(c1, c2, c3));

        TreeMap<String, Integer> res = new TreeMap<>();
        res.put("a", 5);
        res.put("b", 8);
        res.put("c", 9);
        res.put("d", 2);
        res.put("e", 4);

        assertArrayEquals(res.entrySet().toArray(),
                SecondPartTasks.calculateGlobalOrder(orders).entrySet().toArray());
    }
}