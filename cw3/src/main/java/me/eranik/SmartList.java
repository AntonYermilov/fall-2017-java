package me.eranik;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of array list that optimally stores arrays of small size.
 * @param <E>
 */
public class SmartList<E> extends AbstractList<E> implements List<E> {

    private int size;
    private Object storage;

    /**
     * Standard constructor of empty list.
     */
    public SmartList() {
        size = 0;
        storage = null;
    }

    /**
     * Construct new list from specified collection.
     * @param c specified collection
     */
    @SuppressWarnings("unchecked")
    public SmartList(Collection<? extends  E> c) {
        size = c.size();
        if (size == 0) {
            storage = null;
            return;
        }
        if (size == 1) {
            storage = c.iterator().next();
            return;
        }
        if (size <= 5) {
            storage = new Object[5];
            int i = 0;
            for (E e : c) {
                ((E[]) storage)[i] = e;
                i++;
            }
            return;
        }
        storage = new ArrayList<>(c);
    }

    /**
     * Returns element at the specified position.
     * @param i position
     * @return element at the specified position
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            return (E) storage;
        }
        if (size <= 5) {
            return ((E[]) storage)[i];
        }
        return ((ArrayList<E>) storage).get(i);
    }

    /**
     * Sets element at the specified position to the new one.
     * @param i specified position
     * @param e new element
     * @return previous element
     */
    @Override
    @SuppressWarnings("unchecked")
    public E set(int i, @NotNull E e) {
        if (i < 0 || i >= size ) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            E previous = (E) storage;
            storage = e;
            return previous;
        }
        if (size <= 5) {
            E previous = ((E[]) storage)[i];
            ((E[]) storage)[i] = e;
            return previous;
        }
        return ((ArrayList<E>) storage).set(i, e);
    }

    /**
     * Inserts new element on the specified position.
     * @param i specified position
     * @param e new element
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(int i, E e) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            size++;
            storage = e;
            return;
        }
        if (size == 1) {
            E first = (E) storage;
            size++;
            if (i == 0) {
                storage = new Object[]{e, first, null, null, null};
            } else {
                storage = new Object[]{first, e, null, null, null};
            }
            return;
        }
        if (size < 5) {
            System.arraycopy(((E[]) storage), i, ((E[]) storage), i + 1, size - i);
            size++;
            ((E[]) storage)[i] = e;
            return;
        }
        if (size == 5) {
            storage = new ArrayList<E>(Arrays.asList((E[]) storage));
        }
        size++;
        ((ArrayList<E>) storage).add(i, e);
    }

    /**
     * Removes element from the specified position and shifts tail left.
     * @param i specified position
     * @return previous element on that position
     */
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            size = 0;
            E previous = (E) storage;
            storage = null;
            return previous;
        }
        if (size <= 5) {
            E[] array = (E[]) storage;
            E previous = array[i];
            for (int pos = i; pos + 1 < size; pos++) {
                array[pos] = array[pos + 1];
            }

            size--;
            if (size == 1) {
                storage = array[0];
            } else {
                array[size] = null;
                storage = array;
            }
            return previous;
        }

        size--;
        E previous = ((ArrayList<E>) storage).remove(i);
        if (size == 5) {
            storage = ((ArrayList<E>) storage).toArray();
        }

        return previous;
    }

    /**
     * Returns index of the specified object
     * @param o specified object
     * @return index of this object
     */
    @Override
    @SuppressWarnings("unchecked")
    public int indexOf(Object o) {
        if (size == 0) {
            return -1;
        }
        if (size == 1) {
            if (storage.equals(o)) {
                return 0;
            }
            return -1;
        }
        if (size <= 5) {
            for (int i = 0; i < size; i++) {
                if (((E[]) storage)[i].equals(o)) {
                    return i;
                }
            }
            return -1;
        }
        return ((ArrayList<E>) storage).indexOf(o);
    }

    /**
     * Returns size of array.
     * @return size of array
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if list is empty; false otherwise.
     * @return true if list is empty; false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if list contains specified object; false otherwise.
     * @param o specified object
     * @return true if list contains specified object; false otherwise.
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Adds element to the list.
     * @param e new element
     * @return true
     */
    @Override
    public boolean add(E e) {
        add(size, e);
        return true;
    }

    /**
     * Removes specified element from the list.
     * @param o specified element
     * @return true if list contained this element
     */
    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i != -1) {
            remove(i);
            return true;
        }
        return false;
    }

    /**
     * Clears list.
     */
    @Override
    public void clear() {
        size = 0;
        storage = null;
    }

}
