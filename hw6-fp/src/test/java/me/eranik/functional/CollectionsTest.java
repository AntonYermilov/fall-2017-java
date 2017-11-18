package me.eranik.functional;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {
    @Test
    void testMap() {
        Function1<Integer, String> f = new Function1<Integer, String>() {
            @Override
            public String apply(Integer x) {
                return String.valueOf(x + 1);
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3));
        ArrayList<String> newValues = Collections.map(f, values);

        assertArrayEquals(new String[]{"2", "3", "4"}, newValues.toArray());
    }

    @Test
    void testFilter() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3 ,4));
        ArrayList<Integer> newValues = Collections.filter(f, values);

        assertArrayEquals(new Integer[]{2, 4}, newValues.toArray());
    }

    @Test
    void testTakeWhile() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x < 5;
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(3, 4, 5, 6));
        ArrayList<Integer> newValues = Collections.takeWhile(f, values);

        assertArrayEquals(new Integer[]{3, 4}, newValues.toArray());
    }

    @Test
    void takeUnless() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x < 5;
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(6, 5, 4, 3));
        ArrayList<Integer> newValues = Collections.takeUnless(f, values);

        assertArrayEquals(new Integer[]{6, 5}, newValues.toArray());
    }

    @Test
    void testFoldr() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x / y;
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(18, 6, 24, 60));
        int result = Collections.foldr(f, 5, values);

        assertEquals(6, result);
    }

    @Test
    void testFoldl() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x / y;
            }
        };

        ArrayList<Integer> values = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
        int result = Collections.foldl(f, 720, values);

        assertEquals(6, result);
    }

}