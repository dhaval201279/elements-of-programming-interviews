public class StackUsingArray {
    private int [] stack;
    private int capacity;
    private int top = -1;

    /**
     * A stack follows LIFO (Last In, First Out) ordering. We use an array to store elements and a top pointer to track
     * the last added element.
     *
     * Operation	Time Complexity
     * push()	O(1)
     * pop()	O(1)
     * peek()	O(1)
     *
     * @param capacity
     */
    private StackUsingArray(int capacity) {
        if(capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.stack = new int[capacity];
        this.capacity = capacity;
        this.top = -1;
    }

    private boolean isEmpty() {
        return top == -1;
    }

    private boolean isFull() {
        return top == capacity;
    }

    public void push(int element){
        if(isFull()) {
            System.out.println("Stack overflow");
            return;
        }
        stack[++top] = element;
    }

    public int pop() {
        if(isEmpty()) {
            System.out.println("Stack underflow");
            return -1;
        }
        return stack[top--];
    }

    public int peek() {
        if(isEmpty()) {
            System.out.println("Stack underflow - nothing to peek");
            return -1;
        }
        return stack[top];
    }

    public void display() {
        if(isEmpty()) {
            System.out.println("Stack underflow - nothing to display");
            return;
        }
        System.out.println("Stack elements : ");
        for(int i=0; i <= top; i++){
            System.out.println(stack[i] + " ");
        }
    }

    public static void main(String[] args) {
        StackUsingArray stack = new StackUsingArray(5);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.display();
        System.out.println("Popped: " + stack.pop());
        System.out.println("Top element: " + stack.peek());
        stack.display();
    }
}
