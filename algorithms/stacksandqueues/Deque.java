import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<T> implements Iterable<T> {

    private Node first;
    private Node last;
    private int length;

    // construct an empty deque
    public Deque() {
        length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(T item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = this.first;
        Node first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null) oldFirst.prev = first;
        else this.last = first;
        this.first = first;
        length++;
    }

    // add the item to the end
    public void addLast(T item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = this.last;
        Node last = new Node();
        last.item = item;
        last.prev = oldLast;
        if (oldLast != null) oldLast.next = last;
        else this.first = last;
        this.last = last;
        length++;
    }

    // remove and return the item from the front
    public T removeFirst() {
        if (this.isEmpty()) throw new IllegalArgumentException();
        Node removed = first;
        if (first.next != null) {
            first = first.next;
            first.prev = null;
        }
        else first = null;
        length--;
        return removed.item;
    }

    // remove and return the item from the end
    public T removeLast() {
        if (this.isEmpty()) throw new IllegalArgumentException();
        Node removed = last;
        if (last.prev != null) {
            last = last.prev;
            last.next = null;
        }
        else last = null;
        length--;
        return removed.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    @Override
    public String toString() {
        String res = "";
        Node n = this.first;
        while (n.next != null) {
            res += n.item + " ";
            n = n.next;
        }
        res += n.item;
        return res;
    }

    private class DequeIterator implements Iterator<T> {

        private Node curr;
        @Override
        public boolean hasNext() {
            return curr.next != null;
        }

        @Override
        public T next() {
            if (isEmpty()) throw new NoSuchElementException();
            T item = curr.item;
            curr = curr.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        Node next;
        Node prev;
        T item;

        Node() {}
        Node(T item) {
            this.item = item;
        }
    }


    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 3; i++) {
            deque.addFirst(i);
            System.out.println(deque);
            System.out.println("size: " + deque.size());
            deque.addLast(i);
            System.out.println(deque);
            System.out.println("size: " + deque.size());
        }
        for (int i = 0; i < 5; i++) {
            deque.removeFirst();
            System.out.println(deque);
        }
    }
}