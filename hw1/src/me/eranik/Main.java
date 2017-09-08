package me.eranik;

import me.eranik.algorithm.HashTable;

public class Main {
    public static void main(String[] args) {
        new Main().testHashTable();
    }

    public void testHashTable() {
        HashTable storage = new HashTable();
        System.out.println("put (abc, 1): " + storage.put("abc", "1"));
        System.out.println("put (def, 2): " + storage.put("def", "2"));
        System.out.println("put (ghi, 100500): " + storage.put("ghi", "100500"));
        System.out.println("size: " + storage.size());
        System.out.println("contains def: " + storage.contains("def"));
        System.out.println("contains fgh: " + storage.contains("fgh"));
        System.out.println("get abc: " + storage.get("abc"));
        System.out.println("get ghi: " + storage.get("ghi"));
        System.out.println("get abacaba: " + storage.get("abacaba"));
        System.out.println("put (aaa, bbb): " + storage.put("aaa", "bbb"));
        System.out.println("put (bbb, ccc): " + storage.put("bbb", "ccc"));
        System.out.println("put (aaa, ddd): " + storage.put("aaa", "ddd"));
        System.out.println("put (ghi, 100501): " + storage.put("ghi", "100501"));
        System.out.println("remove efg: " + storage.remove("efg"));
        System.out.println("remove def: " + storage.remove("def"));
        System.out.println("size: " + storage.size());
        storage.clear();
        System.out.println("clear");
        System.out.println("size: " + storage.size());
    }
}