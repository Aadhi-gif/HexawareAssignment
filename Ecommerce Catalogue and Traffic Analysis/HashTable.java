import java.util.*;

class Task2_HashTable {

    public static void main(String[] args) {

        // Hash Table with Chaining
        LinkedList<String>[] table = new LinkedList[10];

        for (int i = 0; i < 10; i++)
            table[i] = new LinkedList<>();

        // Insert products
        String[] products = {"A", "K", "B", "C"};

        for (String s : products) {
            int index = Math.floorMod(s.hashCode(), 10);
            table[index].add(s);
        }

        System.out.println("Hash Table:");

        for (int i = 0; i < 10; i++) {
            System.out.println(i + " : " + table[i]);
        }

        System.out.println("A and K have a collision.");

        // HashMap comparison
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < 100000; i++) {
            map.put("SKU" + i, "Product" + i);
        }

        long start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            map.get("SKU" + i);
        }

        long end = System.nanoTime();

        System.out.println("HashMap lookup time: "
                + (end - start) / 1000000.0 + " ms");
    }
}