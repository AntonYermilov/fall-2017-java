package me.eranik.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Function2Test {
    private Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer x, Integer y) {
            return 2 * x * x + y * y;
        }
    };
    private Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer x) {
            return x * x;
        }
    };

    @Test
    void testCompose() {
        assertEquals(1156, f.compose(g).apply(3, 4).intValue());
    }

    @Test
    void testBind1() {
        assertEquals(22, f.bind1(3).apply(2).intValue());
        assertEquals(27, f.bind1(3).apply(3).intValue());
        assertEquals(34, f.bind1(3).apply(4).intValue());
    }

    @Test
    void testBind1Composition() {
        assertEquals(484, f.bind1(3).compose(g).apply(2).intValue());
        assertEquals(729, f.bind1(3).compose(g).apply(3).intValue());
        assertEquals(1156, f.bind1(3).compose(g).apply(4).intValue());
    }

    @Test
    void testBind2() {
        assertEquals(17, f.bind2(3).apply(2).intValue());
        assertEquals(27, f.bind2(3).apply(3).intValue());
        assertEquals(41, f.bind2(3).apply(4).intValue());
    }

    @Test
    void testBind2Composition() {
        assertEquals(289, f.bind2(3).compose(g).apply(2).intValue());
        assertEquals(729, f.bind2(3).compose(g).apply(3).intValue());
        assertEquals(1681, f.bind2(3).compose(g).apply(4).intValue());
    }

    @Test
    void testCurry() {
        assertEquals(17, f.curry(3).apply(2).intValue());
        assertEquals(27, f.curry(3).apply(3).intValue());
        assertEquals(41, f.curry(3).apply(4).intValue());
    }

    @Test
    void testCurryComposition() {
        assertEquals(289, f.curry(3).compose(g).apply(2).intValue());
        assertEquals(729, f.curry(3).compose(g).apply(3).intValue());
        assertEquals(1681, f.curry(3).compose(g).apply(4).intValue());
    }

}