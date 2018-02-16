package me.eranik.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultStackTest {
    private DefaultStack<Integer> stack;

    @BeforeEach
    void initStack() {
        stack = new DefaultStack<>();
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, stack.size());
    }

    @Test
    void testSizeAfterPush() {
        stack.push(5);
        assertEquals(1, stack.size());
        stack.push(5);
        assertEquals(2, stack.size());
        stack.push(55);
        assertEquals(3, stack.size());
    }

    @Test
    void testSizeAfterPushManyNumbers() {
        for (int i = 1; i <= 1025; i++) {
            stack.push(i);
            assertEquals(i, stack.size());
        }
    }

    @Test
    void testSizeAfterPop() {
        stack.push(5);
        stack.push(5);
        stack.push(55);
        stack.pop();
        assertEquals(2, stack.size());
        stack.pop();
        assertEquals(1, stack.size());
        stack.pop();
        assertEquals(0, stack.size());
    }

    @Test
    void testSizeAfterPopManyNumbers() {
        for (int i = 1; i <= 1025; i++) {
            stack.push(i);
        }
        for (int i = 1; i <= 1025; i++) {
            stack.pop();
            assertEquals(1025 - i, stack.size());
        }
    }

    @Test
    void testEmptyEmpty() {
        assertTrue(stack.empty());
    }

    @Test
    void testEmptyAfterPush() {
        stack.push(5);
        assertFalse(stack.empty());
        stack.push(5);
        assertFalse(stack.empty());
        stack.push(55);
        assertFalse(stack.empty());
    }

    @Test
    void testEmptyAfterPushManyNumbers() {
        for (int i = 1; i <= 1025; i++) {
            stack.push(i);
            assertFalse(stack.empty());
        }
    }

    @Test
    void testEmptyAfterPop() {
        stack.push(5);
        stack.push(5);
        stack.push(55);
        stack.pop();
        assertFalse(stack.empty());
        stack.pop();
        assertFalse(stack.empty());
        stack.pop();
        assertTrue(stack.empty());
    }

    @Test
    void testEmptyAfterPopManyNumbers() {
        for (int i = 1; i <= 1025; i++) {
            stack.push(i);
        }
        for (int i = 1; i <= 1024; i++) {
            stack.pop();
            assertFalse(stack.empty());
        }
        stack.pop();
        assertTrue(stack.empty());
    }

    @Test
    void testPushAndTop() {
        stack.push(5);
        assertEquals(5, (int) stack.top());
        stack.push(5);
        assertEquals(5, (int) stack.top());
        stack.push(55);
        assertEquals(55, (int) stack.top());
    }

    @Test
    void testPushAndTopManyNumbers() {
        for (int i = 0; i < 100; i++) {
            stack.push(i);
            assertEquals(i, (int) stack.top());
        }
    }

    @Test
    void testPop() {
        stack.push(5);
        stack.push(55);
        stack.push(555);
        assertEquals(555, (int) stack.pop());
        assertEquals(55, (int) stack.pop());
        assertEquals(5, (int) stack.pop());
    }

    @Test
    void testPopManyNumbers() {
        for (int i = 1; i <= 1025; i++) {
            stack.push(i);
        }
        for (int i = 1; i <= 1025; i++) {
            assertEquals(1026 - i, (int) stack.pop());
        }
    }

    @Test
    void testPopEmpty() {
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    void testPushPopTopComplex() {
        for (int t = 0; t < 10; t++) {
            for (int i = t * 1024; i < (t + 1) * 1024; i++) {
                stack.push(i);
                assertEquals(i, (int) stack.top());
            }
            for (int i = 0; i < 512; i++) {
                assertEquals((t + 1) * 1024 - i - 1, (int) stack.pop());
            }
        }

        for (int t = 0; t < 10; t++) {
            for (int i = 0; i < 512; i++) {
                assertEquals((9 - t) * 1024 + 512 - i - 1, (int) stack.pop());
            }
        }
    }

    @Test
    void testClearEmpty() {
        stack.clear();
        assertTrue(stack.empty());
    }

    @Test
    void testClearAfterPush() {
        stack.push(5);
        stack.push(5);
        stack.push(55);
        stack.clear();
        assertTrue(stack.empty());
    }

    @Test
    void testClearAfterPushManyNumbers() {
        for (int i = 0; i < 1025; i++) {
            stack.push(i);
        }
        stack.clear();
        assertTrue(stack.empty());
    }

}