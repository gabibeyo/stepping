package com.imperva.stepping;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by gabi.beyo on 1/31/2018.
 */
class Q<T> {

    private BlockingQueue<T> blockingQueue;
    private int capacity;

    Q() {
        this(0);
    }

    Q(int capacity) {
        this.capacity = capacity;
        if (capacity > 0)
            blockingQueue = new LinkedBlockingDeque<>(capacity);
        else if (capacity < 0)
            throw new SteppingException("Q capacity must be a positive number");
        else
            blockingQueue = new LinkedBlockingDeque<>();
    }

    T peek() {
        return blockingQueue.peek();
    }

    void queue(T item) {
        try {
            blockingQueue.put(item);
        } catch (Exception e) {
            throw new SteppingSystemException("Queue was not able to insert a new item", e);
        }
    }

    boolean contains() {
        return !blockingQueue.isEmpty();
    }

    T take() throws InterruptedException {
        T data = blockingQueue.take();
        return data;
    }


    int size() {
        return blockingQueue.size();
    }

    void clear() {
        blockingQueue.clear();
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean offer(T message) {
        try {
        return blockingQueue.offer(message);
        } catch (Exception e) {
            throw new SteppingSystemException("Queue was not able to Offer a new item", e);
        }
    }
}

