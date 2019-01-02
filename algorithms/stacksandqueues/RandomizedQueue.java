import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INIT_CAPACITY = 8;

    private Item[] arr;
    private int capacity;
    private int length;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.length = 0;
        this.capacity = INIT_CAPACITY;
        arr = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return length;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        resize();

        arr[length] = item;
        length++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.size() == 0) throw new NoSuchElementException();
        resize();

        int idx = StdRandom.uniform(0, length);
        Item item = arr[idx];
        arr[idx] = arr[length-1];
        arr[length-1] = null;
        length--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.size() == 0) throw new NoSuchElementException();
        return arr[StdRandom.uniform(0, length)];
    }

    private void resize() {
        Item[] newArr;
        if (length == capacity) {
            this.capacity = capacity * 2;
            newArr = (Item[]) new Object[capacity];
        }
        else if (length == capacity / 4 && capacity > INIT_CAPACITY) {
            this.capacity = capacity / 2;
            newArr = (Item[]) new Object[capacity];
        }
        else return;

        int idx = 0;
        for (Item item : this) {
            if (item != null) {
                newArr[idx] = item;
                idx++;
            }
        }
        this.arr = newArr;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] queue;
        private int idx;

        public RandomizedQueueIterator() {
            this.queue = (Item[]) new Object[length];
            for (int i = 0; i < length; i++) {
                this.queue[i] = arr[i];
            }
            StdRandom.shuffle(queue);
            idx = 0;
        }

        @Override
        public boolean hasNext() {
            if (length == 0) throw new NoSuchElementException();
            return idx < length;
        }

        @Override
        public Item next() {
            return queue[idx++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            res.append(arr[i]);
            res.append(" ");
        }
        return res.toString();
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
            System.out.println(queue);
        }

        for (int i : queue) {
            System.out.print(i + " ");
        }
        System.out.println("\n");
        for (int i : queue) {
            System.out.print(i + " ");
        }
        System.out.println("\n");

        for (int i = 0; i < 10; i++) {
            System.out.println(queue.dequeue());
            System.out.println(queue);
        }
    }
}