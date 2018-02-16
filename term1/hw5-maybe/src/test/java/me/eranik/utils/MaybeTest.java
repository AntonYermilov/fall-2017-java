package me.eranik.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaybeTest {

    private Maybe<Integer> justInteger = Maybe.just(5);
    private Maybe<String> justString = Maybe.just("abc");

    private Maybe<Integer> nothingInteger = Maybe.nothing();
    private Maybe<String> nothingString = Maybe.nothing();

    @Test
    void testGetJust() throws AccessToNothingException {
        assertEquals(5, justInteger.get().intValue());
        assertEquals("abc", justString.get());
    }

    @Test
    void testGetNothing() {
        assertThrows(AccessToNothingException.class, nothingInteger::get);
        assertThrows(AccessToNothingException.class, nothingString::get);
    }

    @Test
    void testIsPresentJust() {
        assertTrue(justInteger.isPresent());
        assertTrue(justString.isPresent());
    }

    @Test
    void testIsPresentNothing() {
        assertFalse(nothingInteger.isPresent());
        assertFalse(nothingString.isPresent());
    }

    @Test
    void testMapJust() throws AccessToNothingException {
        Maybe<String> justNewString = justInteger.map(x -> String.valueOf(x * x));
        Maybe<Integer> justNewInteger = justString.map(String::length);

        assertTrue(justNewString.isPresent());
        assertEquals("25", justNewString.get());
        assertTrue(justNewInteger.isPresent());
        assertEquals(3, justNewInteger.get().intValue());
    }

    @Test
    void testMaybeNothing() {
        Maybe<String> nothingNewString = nothingInteger.map(x -> String.valueOf(x * x));
        Maybe<Integer> nothingNewInteger = nothingString.map(String::length);

        assertFalse(nothingNewString.isPresent());
        assertFalse(nothingNewInteger.isPresent());
    }

    @Test
    void convertToInteger() throws AccessToNothingException {
        String number = "123";
        String word = "abc";

        Maybe<Integer> maybeNumber1 = Maybe.convertToInteger(number);
        Maybe<Integer> maybeNumber2 = Maybe.convertToInteger(word);

        assertTrue(maybeNumber1.isPresent());
        assertEquals(123, maybeNumber1.get().intValue());

        assertFalse(maybeNumber2.isPresent());
    }

}