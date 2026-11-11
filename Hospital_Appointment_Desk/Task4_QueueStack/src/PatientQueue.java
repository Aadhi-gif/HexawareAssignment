public class PatientQueue {
    private final String[] data;
    private int front;
    private int rear;
    private int size;

    public PatientQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        data = new String[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(String patientId) {
        if (isFull()) throw new IllegalStateException("Queue Overflow");
        rear = (rear + 1) % data.length;
        data[rear] = patientId;
        size++;
    }

    public String dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue Underflow");
        String patientId = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return patientId;
    }

    public String peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return data[front];
    }

    public boolean isEmpty() { return size == 0; }
    public boolean isFull() { return size == data.length; }
    public int size() { return size; }
}
