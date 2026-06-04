package com.ileja.aibase.common;

import java.io.Serializable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class LoopQueue<T> implements Serializable {
    private static final long serialVersionUID = -3670496550272478781L;
    private int DEFAULT_SIZE;
    private int capacity;
    private Object[] elementData;
    private int front;
    private int rear;

    public LoopQueue() {
        this.DEFAULT_SIZE = 10;
        this.front = 0;
        this.rear = 0;
        this.capacity = 10;
        this.elementData = new Object[10];
    }

    public void add(T t) {
        int i = this.rear;
        int i2 = this.front;
        if (i == i2 && this.elementData[i2] != null) {
            throw new IndexOutOfBoundsException("队列已满的异常");
        }
        Object[] objArr = this.elementData;
        int i3 = this.rear;
        int i4 = i3 + 1;
        this.rear = i4;
        objArr[i3] = t;
        if (i4 == this.capacity) {
            i4 = 0;
        }
        this.rear = i4;
    }

    public void clear() {
        Arrays.fill(this.elementData, (Object) null);
        this.front = 0;
        this.rear = 0;
    }

    public T element() {
        if (isEmpty()) {
            return null;
        }
        return (T) this.elementData[this.front];
    }

    public void forceAdd(T t) {
        if (getCapacity() == size()) {
            remove();
        }
        add(t);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public T getRear() {
        int i = this.rear;
        if (i == 0) {
            i = this.capacity;
        }
        return (T) this.elementData[i - 1];
    }

    public boolean isEmpty() {
        int i = this.rear;
        return i == this.front && this.elementData[i] == null;
    }

    public T remove() {
        if (isEmpty()) {
            return null;
        }
        Object[] objArr = this.elementData;
        int i = this.front;
        T t = (T) objArr[i];
        int i2 = i + 1;
        this.front = i2;
        objArr[i] = null;
        if (i2 == this.capacity) {
            i2 = 0;
        }
        this.front = i2;
        return t;
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        }
        int i = this.rear;
        int i2 = this.front;
        return i > i2 ? i - i2 : this.capacity - (i2 - i);
    }

    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        if (this.front < this.rear) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = this.front; i < this.rear; i++) {
                sb.append(this.elementData[i].toString() + ", ");
            }
            int length = sb.length();
            StringBuilder sbDelete = sb.delete(length - 2, length);
            sbDelete.append("]");
            return sbDelete.toString();
        }
        StringBuilder sb2 = new StringBuilder("[");
        for (int i2 = this.front; i2 < this.capacity; i2++) {
            sb2.append(this.elementData[i2].toString() + ", ");
        }
        for (int i3 = 0; i3 < this.rear; i3++) {
            sb2.append(this.elementData[i3].toString() + ", ");
        }
        int length2 = sb2.length();
        StringBuilder sbDelete2 = sb2.delete(length2 - 2, length2);
        sbDelete2.append("]");
        return sbDelete2.toString();
    }

    public T element(int i) {
        if (isEmpty()) {
            return null;
        }
        int i2 = this.rear;
        int i3 = this.front;
        int i4 = this.capacity;
        int i5 = ((i2 - i3) + i4) % i4;
        if (i < 0 || i > i5) {
            return null;
        }
        return (T) this.elementData[(i3 + i) % i4];
    }

    public LoopQueue(int i) {
        this.DEFAULT_SIZE = 10;
        this.front = 0;
        this.rear = 0;
        this.capacity = i;
        this.elementData = new Object[i];
    }

    public LoopQueue(T t) {
        this();
        this.elementData[0] = t;
        this.rear++;
    }

    public LoopQueue(T t, int i) {
        this.DEFAULT_SIZE = 10;
        this.front = 0;
        this.rear = 0;
        this.capacity = i;
        Object[] objArr = new Object[i];
        this.elementData = objArr;
        objArr[0] = t;
        this.rear = 0 + 1;
    }
}