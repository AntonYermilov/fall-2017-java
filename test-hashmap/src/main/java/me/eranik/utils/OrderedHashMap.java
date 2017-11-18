package me.eranik.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class OrderedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private int size = 0;
    private int capacity = 10;
    private Node<K, V> listHead = new Node();
    private Node<K, V>[] hashtable = new Node[capacity];

    private int getHash(K key, int mod) {
        return key.hashCode() % mod;
    }

    private void rebuild() {
        if (size * 2 <= capacity)
            return;
        Node[] newHashtable = new Node[2 * capacity];
        for (int i = 0; i < capacity; i++) {
            if (hashtable[i] == null)
                continue;
            for (Node<K, V> node = hashtable[i].next; node != hashtable[i]; node = node.next) {
                int hash = getHash(node.key, 2 * capacity);
                if (newHashtable[hash] == null)
                    newHashtable[hash] = new Node();
                newHashtable[getHash(node.key, 2 * capacity)]
                        .addAfter(new Node(node.key, node.value, node.link));
            }
        }
        capacity *= 2;
        hashtable = newHashtable;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new OrderedSet<>(new OrderedHashMapIterator());
    }

    @Override
    public V put(K key, V value) {
        rebuild();
        int hash = getHash(key, capacity);
        if (hashtable[hash] == null)
            hashtable[hash] = new Node();

        for (Node<K, V> node = hashtable[hash].next; node != hashtable[hash]; node = node.next) {
            if (node.key.equals(key)) {
                V previous = node.value;

                node.value = value;
                node.link.remove();
                listHead.prev.addAfter(new Node(key, value));
                node.link = listHead.prev;

                return previous;
            }
        }

        hashtable[hash].addAfter(new Node(key, value));
        listHead.prev.addAfter(new Node(key, value));
        hashtable[hash].next.link = listHead.prev;
        return null;
    }

    private class Node<K, V> {
        private Node next = this;
        private Node prev = this;
        private Node link = this;
        private K key = null;
        private V value = null;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node(K key, V value, Node link) {
            this.key = key;
            this.value = value;
            this.link = link;
        }

        public void addAfter(Node node) {
            node.next = next;
            next.prev = node;
            this.next = node;
            node.prev = this;
        }

        public void remove() {
            next.prev = null;
            next.next = null;
            next.link = null;

            next = next.next;
            next.prev = this;
        }
    }

    private class OrderedHashMapIterator implements Iterator {
        private Node<K, V> head = listHead;

        @Override
        public boolean hasNext() {
            return head.next != listHead;
        }

        @Override
        public V next() {
            V value = head.value;
            head = head.next;
            return value;
        }
    }

    private class OrderedSet<V> extends AbstractSet<V> implements Set<V> {
        Iterator<V> iterator;

        public OrderedSet(Iterator<V> iterator) {
            this.iterator = iterator;
        }

        @Override
        public Iterator<V> iterator() {
            return iterator;
        }

        @Override
        public int size() {
            size = 0;
            while (iterator.hasNext()) {
                size++;
                iterator.next();
            }
            return size;
        }
    }

    @Override
    public V getOrDefault(Object o, V v) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {

    }

    @Override
    public V putIfAbsent(K k, V v) {
        return null;
    }

    @Override
    public boolean remove(Object o, Object o1) {
        return false;
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        return false;
    }

    @Override
    public V replace(K k, V v) {
        return null;
    }

    @Override
    public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
        return null;
    }

    @Override
    public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        return null;
    }

    @Override
    public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        return null;
    }

    @Override
    public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        return null;
    }
}
