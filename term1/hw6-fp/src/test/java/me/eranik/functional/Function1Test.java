package me.eranik.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Function1Test {
    @Test
    void testComposeTwoFunctions() {
        Function1<Integer, Integer> f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 3;
            }
        };
        Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x * 8;
            }
        };

        assertEquals(144, f.compose(g).apply(15).intValue());
        assertEquals(123, g.compose(f).apply(15).intValue());
        assertEquals(21, f.compose(f).apply(15).intValue());
    }

    @Test
    void testComposeThreeFunctions() {
        Function1<Integer, String> f = new Function1<Integer, String>() {
            @Override
            public String apply(Integer x) {
                return String.valueOf(x + 3);
            }
        };
        Function1<String, Integer> g = new Function1<String, Integer>() {
            @Override
            public Integer apply(String x) {
                return Integer.valueOf(x + "0");
            }
        };

        assertEquals("183", f.compose(g).compose(f).apply(15));
        assertEquals(1530, g.compose(f).compose(g).apply("15").intValue());
    }

}