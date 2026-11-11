public class QueueStackDemo {
    public static void main(String[] args) {
        System.out.println("WALK-IN TOKEN QUEUE (FIFO)");
        PatientQueue queue = new PatientQueue(5);
        queue.enqueue("P101");
        queue.enqueue("P102");
        queue.enqueue("P103");
        System.out.println("Next patient: " + queue.peek());
        System.out.println("Serving: " + queue.dequeue());
        System.out.println("Next patient: " + queue.peek());

        System.out.println("\nRECENTLY VIEWED PATIENTS (LIFO)");
        PatientStack stack = new PatientStack(5);
        stack.push("P101");
        stack.push("P102");
        stack.push("P103");
        System.out.println("Most recently viewed: " + stack.peek());
        System.out.println("Removing recent history: " + stack.pop());
        System.out.println("Now most recent: " + stack.peek());
    }
}
