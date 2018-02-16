package me.eranik.util;

import org.jetbrains.annotations.NotNull;

import java.util.EmptyStackException;

/**
 * Default implementation of stack interface.
 */
public class DefaultStack<T> implements Stack<T> {
    private int size = 0;
    private Node top = new Node(null, null);

    /**
     * Returns size of the stack.
     * @return size of the stack
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks is stack is empty.
     * @return {@code true} if stack is empty; {@code false} otherwise
     */
    @Override
    public boolean empty() {
        return size == 0;
    }

    /**
     * Adds value to the top of the stack.
     * @param value specified value
     */
    @Override
    public void push(@NotNull T value) {
        top = new Node(value, top);
        size++;
    }

    /**
     * Removes element from the top of the stack.
     * @return removed element
     */
    @Override
    public T pop() {
        if (top.previous == null) {
            throw new EmptyStackException();
        }
        T topValue = top.value;
        top = top.previous;
        size--;
        return topValue;
    }

    /**
     * Returns element from the top of the stack.
     * @return element from the top of the stack
     */
    @Override
    public T top() {
        return top.value;
    }

    /**
     * Removes all elements from the stack.
     */
    @Override
    public void clear() {
        size = 0;
        top = new Node(null, null);
    }

    private class Node {
        T value;
        Node previous;

        Node(T value, Node previous) {
            this.value = value;
            this.previous = previous;
        }
    }

}
