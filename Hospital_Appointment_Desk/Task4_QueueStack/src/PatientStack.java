public class PatientStack {
    private final String[] data;
    private int top;

    public PatientStack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        data = new String[capacity];
        top = -1;
    }

    public void push(String patientId) {
        if (isFull()) throw new IllegalStateException("Stack Overflow");
        data[++top] = patientId;
    }

    public String pop() {
        if (isEmpty()) throw new IllegalStateException("Stack Underflow");
        String patientId = data[top];
        data[top--] = null;
        return patientId;
    }

    public String peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return data[top];
    }

    public boolean isEmpty() { return top == -1; }
    public boolean isFull() { return top == data.length - 1; }
    public int size() { return top + 1; }
}
