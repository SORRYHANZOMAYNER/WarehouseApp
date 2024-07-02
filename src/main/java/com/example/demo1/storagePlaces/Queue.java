package com.example.demo1.storagePlaces;

import com.example.demo1.module1.DTO.*;
import com.example.demo1.module1.modules.*;

public class Queue {
    int queueLength = 4;
    Detail items[] = new Detail[queueLength];
    int front = -1;
    int back = -1;
    private final Object lock = new Object(); // Объект для синхронизации

    synchronized boolean isFull() {
        if (back == queueLength - 1) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        if (front == -1 && back == -1) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void enQueue(Detail itemValue) {
        synchronized (lock) {
            while (isFull()) {
                try {
                    lock.wait(); // Ждем, пока очередь не освободится
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
                lock.notify(); // Уведомляем ожидающие потоки
            }
        }
    }

    public synchronized void deQueue() {
        synchronized (lock) {
            while (isEmpty()) {
                try {
                    lock.wait(); // Ждем, пока очередь не заполнится
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (front == back) {
                front = back = -1;
            } else {
                front++;
                lock.notify(); // Уведомляем ожидающие потоки
            }
        }
    }
    synchronized void checkDeliveredDetails() {
        int i;
        if (isEmpty()) {
            System.out.println("Queue is empty");
        } else {
            for (i = front; i <= back; i++) {
                System.out.println(items[i]);
            }
        }
    }

    public synchronized Detail peak() {
        return items[front];
    }
}
