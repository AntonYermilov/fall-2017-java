package me.eranik.algorithm;

import java.lang.Object;

public class HashTable {
    private LinkedList[] table = new LinkedList[1];
    private int capacity = 1;
    private int size = 0;

    /**
     * Calculates a hash of a <code>key</code> string modulo <code>mod</code>.
     *
     * @param key the string for calculating a hash
     * @param mod the value of module
     * @return the hash value modulo <code>mod</code>
     */
    private int getHash(String key, int mod) {
        int hash = key.hashCode() % mod;
        if (hash < 0)
            hash += capacity;
        return hash;
    }

    /**
     * Rebuilds hashtable, increases the capacity if the number of stored
     * elements is too big.
     */
    private void rebuild() {
        if (size * 2 < capacity)
            return;

        int newCapacity = capacity * 2;
        LinkedList[] newTable = new LinkedList[newCapacity];

        for (LinkedList list : table) {
            if (list == null || list.isEmpty())
                continue;

            Object[] arr = list.toArray();
            for (int i = 0; i < arr.length; i++) {
                Data data = (Data) arr[i];
                int hash = getHash(data.key, newCapacity);

                if (newTable[hash] == null)
                    newTable[hash] = new LinkedList();
                newTable[hash].add(data);
            }
        }

        capacity = newCapacity;
        table = newTable;
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
     * Returns <code>true</code> if hashtable contains a <code>key</code> or
     * <code>false</code> otherwise.
     *
     * @param key the key value
     * @return <code>true</code> if hashtable contains a <code>key</code>;
     *         <code>false</code> otherwise
     */
    public boolean contains(String key) {
        int hash = getHash(key, capacity);
        return table[hash] != null && table[hash].contains(new Data(key, null));
    }

    /**
     * Provides an access to the stored value by key.
     *
     * @param key the key value
     * @return the value to which key is mapped, or {@code null} otherwise
     */
    public String get(String key) {
        int hash = getHash(key, capacity);
        if (table[hash] == null)
            return null;

        int index = table[hash].indexOf(new Data(key, null));
        String value = null;
        if (index != -1) {
            value = ((Data) table[hash].get(index)).getValue();
        }
        return value;
    }

    /**
     * Associates the <code>key</code> with the <code>value</code>.
     * If hashtable previously contained the mapping for key, the old
     * value is replaced.
     *
     * @param key the key value
     * @param value the value to be associated with the key
     * @return the previous value associated with the key
     */
    public String put(String key, String value) {
        rebuild();

        int hash = getHash(key, capacity);
        if (table[hash] == null) {
            table[hash] = new LinkedList();
        }

        int index = table[hash].indexOf(new Data(key, null));
        String res = null;
        if (index != -1) {
            res = ((Data) table[hash].get(index)).getValue();
            table[hash].set(index, new Data(key, value));
        } else {
            size++;
            table[hash].add(new Data(key, value));
        }
        return res;
    }

    /**
     * Removes the mapping for the key if present.
     *
     * @param key the key value
     * @return the previous value associated with the key or {@code null}
     * if there was no mapping for the key
     */
    public String remove(String key) {
        int hash = getHash(key, capacity);
        if (table[hash] == null) {
            return null;
        }

        int index = table[hash].indexOf(new Data(key, null));
        String res = null;
        if (index != -1) {
            res = ((Data) table[hash].get(index)).getValue();
            table[hash].remove(index);
            size--;
        }
        return res;
    }

    /**
     * Clears hashtable, removing all stored elements.
     */
    public void clear() {
        for (LinkedList list : table) {
            if (list == null || list.isEmpty())
                continue;
            list.clear();
        }
        size = 0;
    }

    /**
     * Class for storing key-value pairs
     */
    private class Data {
        private String key;
        private String value;

        /**
         * Constructs a new key-value pair with the specified initial values
         * to be used in hashtable.
         *
         * @param key the key value
         * @param value the value to be associated with key
         */
        Data(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Compares two objects for equality.
         *
         * @param other the other object
         * @return true if another object has the same type with the current
         * and their keys are equal; false otherwise
         */
        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (!(other instanceof Data))
                return false;
            Data otherData = (Data) other;
            return key.equals(otherData.key);
        }


        /**
         * @return hash of the object
         */
        @Override
        public int hashCode() {
            return key.hashCode();
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
