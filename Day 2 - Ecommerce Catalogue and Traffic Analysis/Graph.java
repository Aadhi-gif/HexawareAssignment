
import java.util.*;

class Task3_Graph {

    static ArrayList<Integer>[] graph;
    static int vertices = 4;

    // Create graph
    static void addEdge(int from, int to) {
        graph[from].add(to);
    }

    // BFS Topological Sorting
    static void bfs() {

        int[] indegree = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j : graph[i]) {
                indegree[j]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        int count = 0;

        System.out.print("BFS Order: ");

        while (!queue.isEmpty()) {

            int current = queue.poll();

            System.out.print(current + " ");

            count++;

            for (int j : graph[current]) {

                indegree[j]--;

                if (indegree[j] == 0) {
                    queue.add(j);
                }
            }
        }

        if (count != vertices) {
            System.out.println("\nCircular Dependency Found");
        } else {
            System.out.println();
        }
    }

    // DFS Topological Sorting
    static boolean dfs(int vertex, int[] visited,
                       Stack<Integer> stack) {

        visited[vertex] = 1;

        for (int j : graph[vertex]) {

            if (visited[j] == 1) {
                return false;
            }

            if (visited[j] == 0) {

                if (!dfs(j, visited, stack)) {
                    return false;
                }
            }
        }

        visited[vertex] = 2;

        stack.push(vertex);

        return true;
    }

    static void dfsSort() {

        int[] visited = new int[vertices];

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices; i++) {

            if (visited[i] == 0) {

                if (!dfs(i, visited, stack)) {
                    System.out.println("Cycle Found");
                    return;
                }
            }
        }

        System.out.print("DFS Order: ");

        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {

        graph = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        // 0 = Shipping
        // 1 = Inventory
        // 2 = Pricing
        // 3 = Cart

        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 3);

        System.out.println("BFS Deployment Order:");
        bfs();

        System.out.println("DFS Deployment Order:");
        dfsSort();

        // Add circular dependency
        System.out.println("\nAdding Circular Dependency");

        addEdge(3, 0);

        System.out.println("BFS Cycle Detection:");
        bfs();

        System.out.println("DFS Cycle Detection:");
        dfsSort();
    }
}
