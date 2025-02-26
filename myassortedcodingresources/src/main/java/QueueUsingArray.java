public class QueueUsingArray {
    private int [] queue;
    private int capacity;
    private int currentSize;
    private int front;
    private int rear;

    public QueueUsingArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.queue = new int[capacity];
        this.capacity = capacity;
        this.currentSize = 0;
        this.front = 0;
        this.rear = -1;
    }

    private boolean isEmpty() {
        return currentSize == 0;
    }

    private boolean isFull() {
        return currentSize == capacity;
    }

    public void enqueue(int element) {
        if(isFull()) {
            System.out.println("Queue is full");
            return;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = element;
        currentSize++;
    }

    public int dequeue() {
        if(isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        int poppedElement = queue[front];
        front = (front + 1) % capacity;
        currentSize--;
        return poppedElement;
    }

    // Peek at the front element without removing it
    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty. No element to peek.");
            return -1; // Return a default value indicating failure
        }
        return queue[front];
    }

    // Print the queue
    public void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        System.out.print("Queue elements : ");
        for (int i = 0; i < currentSize; i++) {
            int index = (front + i) % capacity;
            System.out.print(queue[index] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        QueueUsingArray queue = new QueueUsingArray(5);

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.printQueue(); // Output: Queue: 10 20 30

        System.out.println("Peek: " + queue.peek()); // Output: Peek: 10

        queue.dequeue();
        queue.printQueue(); // Output: Queue: 20 30

        queue.enqueue(40);
        queue.enqueue(50);
        queue.enqueue(60); // Output: Queue is full. Cannot enqueue 60
        queue.printQueue(); // Output: Queue: 20 30 40 50
    }
}
