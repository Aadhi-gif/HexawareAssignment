import java.util.*;

class Task3_Graph {

    public static void main(String[] args) {

        int vertices = 4;

        ArrayList<Integer>[] graph = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++)
            graph[i] = new ArrayList<>();

        // 0 = Shipping
        // 1 = Inventory
        // 2 = Pricing
        // 3 = Cart

        graph[0].add(1);
        graph[1].add(2);
        graph[2].add(3);

        int[] indegree = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j : graph[i])
                indegree[j]++;
        }

        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0)
                queue.add(i);
        }

        int count = 0;

        System.out.println("Deployment Order:");

        while (!queue.isEmpty()) {

            int current = queue.poll();

            System.out.print(current + " ");
            count++;

            for (int j : graph[current]) {

                indegree[j]--;

                if (indegree[j] == 0)
                    queue.add(j);
            }
        }

        if (count != vertices)
            System.out.println("\nCircular Dependency Found");
        else
            System.out.println("\nNo Circular Dependency");

        // Add circular dependency
        graph[3].add(0);

        System.out.println("After adding circular dependency:");

        // Check cycle again
        int[] degree = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j : graph[i])
                degree[j]++;
        }

        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < vertices; i++) {
            if (degree[i] == 0)
                q.add(i);
        }

        int processed = 0;

        while (!q.isEmpty()) {
            int current = q.poll();
            processed++;

            for (int j : graph[current]) {
                degree[j]--;

                if (degree[j] == 0)
                    q.add(j);
            }
        }

        System.out.println(
                processed != vertices
                        ? "Circular Dependency Found"
                        : "No Circular Dependency"
        );
    }
}