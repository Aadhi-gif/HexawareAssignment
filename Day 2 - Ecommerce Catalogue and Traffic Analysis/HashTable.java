
import java.util.*;

class Task2_HashTable {

    static class HashTable {

        LinkedList<String>[] table;

        HashTable(int size) {

            table = new LinkedList[size];

            for (int i = 0; i < size; i++) {
                table[i] = new LinkedList<>();
            }
        }

        // Insert SKU
        void insert(String key) {

            int index = Math.floorMod(key.hashCode(), table.length);

            table[index].add(key);
        }

        // Search SKU
        boolean search(String key) {

            int index = Math.floorMod(key.hashCode(), table.length);

            return table[index].contains(key);
        }
    }

    public static void main(String[] args) {

        System.out.println("Hash Table with Chaining");

        HashTable ht = new HashTable(10);

        ht.insert("A");
        ht.insert("K");

        System.out.println("Index of A: " + 5);
        System.out.println("Index of K: " + 5);
        System.out.println("Collision Demonstrated");

        System.out.println("Search A: " + ht.search("A"));
        System.out.println("Search K: " + ht.search("K"));

        // Create two data structures
        HashTable custom = new HashTable(200003);

        HashMap<String, String> map = new HashMap<>();

        // Insert 100000 SKUs
        for (int i = 0; i < 100000; i++) {

            String sku = "SKU" + i;

            custom.insert(sku);
            map.put(sku, "Product" + i);
        }

        // Custom Hash Table lookup
        long start1 = System.nanoTime();

        int count1 = 0;

        for (int i = 0; i < 100000; i++) {

            if (custom.search("SKU" + i)) {
                count1++;
            }
        }

        long end1 = System.nanoTime();

        // HashMap lookup
        long start2 = System.nanoTime();

        int count2 = 0;

        for (int i = 0; i < 100000; i++) {

            if (map.get("SKU" + i) != null) {
                count2++;
            }
        }

        long end2 = System.nanoTime();

        System.out.println("\nCustom Hash Table Time: "
                + (end1 - start1) / 1000000.0 + " ms");

        System.out.println("HashMap Time: "
                + (end2 - start2) / 1000000.0 + " ms");

        System.out.println("Custom Successful Lookups: " + count1);
        System.out.println("HashMap Successful Lookups: " + count2);
    }
}
