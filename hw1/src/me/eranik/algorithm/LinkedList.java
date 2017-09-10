package me.eranik.algorithm;

public class LinkedList {
    private Node begin = null;
    private Node end = null;
    private int size = 0;

    /**
     * Appends the specified element to the end of the list
     *
     * @param obj  the element to be appended
     */
    public void add(Object obj) {
        if (size == 0) {
            begin = new Node(null, null, obj);
            end = begin;
        } else {
            Node node = new Node(null, end, obj);
            end.setNext(node);
            end = node;
        }
        size++;
    }

    /**
     * Returns the element at the specified position in list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in list
     */
    public Object get(int index) {
        Node node = getNodeByIndex(index);
        return node == null ? null : node.getObject();
    }

    /**
     * Replaces the element at the specified position with another one.
     *
     * @param index index of the element to replace
     * @param obj element to be stored at the specified position
     */
    public void set(int index, Object obj) {
        Node node = getNodeByIndex(index);
        if (node != null)
            node.setObject(obj);
    }

    /**
     * Removes the element at the specified position in list.
     *
     * @param index index of the element to be removed
     */
    public void remove(int index) {
        Node node = getNodeByIndex(index);
        if (node == null)
            return;

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            end = node.getPrev();
        }
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            begin = node.getNext();
        }
        size--;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * or -1 if this element does not exist.
     *
     * @param obj element to search for
     * @return the index of the first occurrence of the specified element
     *         or -1 if this element does not exist
     */
    public int indexOf(Object obj) {
        int index = 0;
        Node node = begin;
        while (node != null && !node.getObject().equals(obj)) {
            index++;
            node = node.getNext();
        }
        return index == size ? -1 : index;
    }

    /**
     * Returns true if list contains specified object or false otherwise.
     *
     * @param obj the element to be found
     * @return true if list contains specified object or false otherwise
     */
    public boolean contains(Object obj) {
        Node node = begin;
        while (node != null && !node.getObject().equals(obj)) {
            node = node.getNext();
        }
        return node != null;
    }

    /**
     * Returns <code>true</code> if this list contains no elements
     * or <code>false</code> otherwise
     *
     * @return <code>true</code> if this list contains no elements
     *         or <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of stored elements.
     *
     * @return the number of stored elements
     */
    public int size() {
        return size;
    }

    /**
     * Clears list, removing all stored elements.
     */
    public void clear() {
        begin = end = null;
        size = 0;
    }

    /**
     * Converts list to array.
     *
     * @return array of stored objects in proper sequence
     */
    public Object[] toArray() {
        int index = 0;
        Object[] array = new Object[size];
        for (Node node = begin; node != null; node = node.getNext()) {
            array[index++] = node.getObject();
        }
        return array;
    }

    /**
     * Returns the node stored at the specified position.
     *
     * @param index index of the node to return
     * @return the node stored at the specified position
     */
    private Node getNodeByIndex(int index) {
        if (index >= size) {
            return null;
        }

        Node node = begin;
        while (index != 0) {
            node = node.getNext();
        }
        return node;
    }

    /**
     * This class implements the nodes of linked list.
     */
    private class Node {
        private Node next;
        private Node prev;
        private Object obj;

        /**
         * Constructs the node with specified parameters.
         *
         * @param next reference to the next node of linked list
         * @param prev reference to the previous node of linked list
         * @param obj reference to the stored object
         */
        Node(Node next, Node prev, Object obj) {
            this.next = next;
            this.prev = prev;
            this.obj = obj;
        }

        /**
         * Returns the reference to the next node of linked list.
         *
         * @return the reference to the next node of linked list
         */
        public Node getNext() {
            return next;
        }

        /**
         * Replaces the reference to the next node with the new one
         *
         * @param next new reference to the next node of linked list
         */
        public void setNext(Node next) {
            this.next = next;
        }

        /**
         * Returns the reference to the previous node of linked list.
         *
         * @return the reference to the previous node of linked list
         */
        public Node getPrev() {
            return prev;
        }

        /**
         * Replaces the reference to the previous node with the new one
         *
         * @param prev new reference to the previous node of linked list
         */
        public void setPrev(Node prev) {
            this.prev = prev;
        }

        /**
         * Returns the reference to the stored object.
         *
         * @return the reference to the stored object
         */
        public Object getObject() {
            return obj;
        }

        /**
         * Replaces the reference to the stored object with the new one
         *
         * @param obj new reference to the stored object
         */
        public void setObject(Object obj) {
            this.obj = obj;
        }
    }
}
