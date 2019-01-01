import edu.princeton.cs.algs4.Stack;

public class StackQueue<T> {

    private Stack<T> stack1;
    private Stack<T> stack2;

    public void enqueue(T item) {
        stack1.push(item);
    }

    public T dequeue() {
        if (stack2.size() == 0) refill();
        return stack2.pop();
    }

    private void refill() {
        for (T item : stack1) stack2.push(item);
    }

}