package me.eranik.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredicateTest {

    @Test
    void testOrTwoPredicates() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 3 == 0;
            }
        };
        Predicate<Integer> g = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 10 == 8;
            }
        };

        assertTrue(f.or(g).apply(12));
        assertTrue(f.or(g).apply(28));
        assertTrue(f.or(g).apply(18));
        assertFalse(f.or(g).apply(239));
    }


    @Test
    void testOrThreePredicates() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 10 == 3;
            }
        };
        Predicate<Integer> g = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x * 8 < 100;
            }
        };
        Predicate<Integer> h = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 3 == 0;
            }
        };

        assertTrue(f.or(g).or(h).apply(23));
        assertTrue(f.or(g).or(h).apply(11));
        assertTrue(f.or(g).or(h).apply(15));
        assertFalse(f.or(g).or(h).apply(17));
    }

    @Test
    void testOrLazyNoException() {
        Predicate<String> f = new Predicate<String>() {
            @Override
            public Boolean apply(String x) {
                return x.charAt(-1) == 'a';
            }
        };

        assertTrue(Predicate.<String>ALWAYS_TRUE().or(f).apply("aba"));
    }

    @Test
    void testOrLazyException() {
        Predicate<String> f = new Predicate<String>() {
            @Override
            public Boolean apply(String x) {
                return x.charAt(-1) == 'a';
            }
        };

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            Predicate.<String>ALWAYS_FALSE().or(f).apply("aba");
        });
    }

    @Test
    void testAndTwoPredicates() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 3 == 0;
            }
        };
        Predicate<Integer> g = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 10 == 8;
            }
        };

        assertFalse(f.and(g).apply(12));
        assertFalse(f.and(g).apply(28));
        assertTrue(f.and(g).apply(18));
        assertFalse(f.and(g).apply(239));
    }


    @Test
    void testAndThreePredicates() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 10 == 3;
            }
        };
        Predicate<Integer> g = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x * 8 < 100;
            }
        };
        Predicate<Integer> h = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 3 == 0;
            }
        };

        assertFalse(f.and(g).and(h).apply(23));
        assertFalse(f.and(g).and(h).apply(11));
        assertFalse(f.and(g).and(h).apply(15));
        assertFalse(f.and(g).and(h).apply(17));
        assertTrue(f.and(g).and(h).apply(3));
    }

    @Test
    void testAndLazyNoException() {
        Predicate<String> f = new Predicate<String>() {
            @Override
            public Boolean apply(String x) {
                return x.charAt(-1) == 'a';
            }
        };

        assertFalse(Predicate.<String>ALWAYS_FALSE().and(f).apply("aba"));
    }

    @Test
    void testAndLazyException() {
        Predicate<String> f = new Predicate<String>() {
            @Override
            public Boolean apply(String x) {
                return x.charAt(-1) == 'a';
            }
        };

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            Predicate.<String>ALWAYS_TRUE().and(f).apply("aba");
        });
    }

    @Test
    void testNot() {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x < 10;
            }
        };

        assertTrue(f.not().apply(15));
        assertFalse(f.not().apply(5));
        assertTrue(f.not().not().apply(5));
    }

}