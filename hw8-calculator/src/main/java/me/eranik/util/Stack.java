package me.eranik.util;

public interface Stack<T> {
    /**
     * Returns size of the stack.
     * @return size of the stack
     */
    int size();

    /**
     * Checks is stack is empty.
     * @return {@code true} if stack is empty; {@code false} otherwise
     */
    boolean empty();

    /**
     * Adds value to the top of the stack.
     * @param value specified value
     */
    void push(T value);

    /**
     * Removes element from the top of the stack.
     * @return removed element
     */
    T pop();

    /**
     * Returns element from the top of the stack.
     * @return element from the top of the stack
     */
    T top();

    /**
     * Removes all elements from the stack.
     */
    void clear();
}
