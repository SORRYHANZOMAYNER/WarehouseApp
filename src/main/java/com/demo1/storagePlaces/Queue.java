package com.demo1.storagePlaces;

import com.demo1.module1.modules.Detail;
import com.demo1.module1.modules.*;

public class Queue {
    int queueLength = 4;
    Detail[] items = new Detail[queueLength];
    int front = -1;
    int back = -1;
    private final Object lock = new Object();

    synchronized boolean isFull() {
        return back == queueLength - 1;
    }

    public synchronized boolean isEmpty() {
        return front == -1 && back == -1;
    }

    public synchronized void enQueue(Detail itemValue) {
        synchronized (lock) {
            while (isFull()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (front == -1 && back == -1) {
                front = back = 0;
                items[back] = itemValue;
            } else {
                back++;
                items[back] = itemValue;
                lock.notify();
            }
        }
    }

    public synchronized void deQueue() {
        synchronized (lock) {
            while (isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (front == back) {
                front = back = -1;
            } else {
                front++;
                lock.notify();
            }
        }
    }

    public synchronized Detail peak() {
        return items[front];
    }
}
