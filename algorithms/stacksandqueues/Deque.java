import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

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
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = this.first;
        Node f = new Node();
        f.item = item;
        f.next = oldFirst;
        if (oldFirst != null) oldFirst.prev = f;
        else this.last = f;
        this.first = f;
        length++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = this.last;
        Node l = new Node();
        l.item = item;
        l.prev = oldLast;
        if (oldLast != null) oldLast.next = l;
        else this.first = l;
        this.last = l;
        length++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Node removed = first;
        if (first.next != null) {
            first = first.next;
            first.prev = null;
        }
        else first = last = null;
        length--;
        return removed.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Node removed = last;
        if (last.prev != null) {
            last = last.prev;
            last.next = null;
        }
        else last = first = null;
        length--;
        return removed.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Node n = this.first;
        while (n.next != null) {
            res.append(n.item);
            res.append(" ");
            n = n.next;
        }
        res.append(n.item);
        return res.toString();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node curr;

        DequeIterator() {
            this.curr = first;
        }

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public Item next() {
            if (isEmpty()) throw new NoSuchElementException();
            Item item = curr.item;
            curr = curr.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Node next;
        private Node prev;
        private Item item;

        Node() { }
        Node(Item item) {
            this.item = item;
        }
    }


    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.removeFirst();

        deque.addFirst(1);
        deque.removeLast();
    }
}