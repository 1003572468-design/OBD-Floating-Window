package com.ileja.aibase.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class FixedSizeQueue<E> implements Queue<E> {
    private int capacity;
    private Object[] elements;
    private int head;
    private int modCount;
    private int size;
    private int tail;

    private class Iter implements Iterator<E> {

        /* JADX INFO: renamed from: c */
        int f5883c;

        /* JADX INFO: renamed from: d */
        int f5884d;

        /* JADX INFO: renamed from: e */
        int f5885e;

        private Iter() {
            this.f5883c = 0;
            this.f5884d = -1;
            this.f5885e = FixedSizeQueue.this.modCount;
        }

        /* JADX INFO: renamed from: a */
        final void m4045a() {
            if (FixedSizeQueue.this.modCount != this.f5885e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f5883c != FixedSizeQueue.this.size();
        }

        @Override // java.util.Iterator
        public E next() {
            m4045a();
            E e = (E) FixedSizeQueue.this.elements[(FixedSizeQueue.this.head + this.f5883c) % FixedSizeQueue.this.capacity];
            int i = this.f5883c;
            this.f5883c = i + 1;
            this.f5884d = i;
            return e;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.f5884d == -1) {
                throw new IllegalStateException();
            }
            m4045a();
            int i = (FixedSizeQueue.this.head + this.f5884d) % FixedSizeQueue.this.capacity;
            if (i >= FixedSizeQueue.this.head) {
                System.arraycopy(FixedSizeQueue.this.elements, FixedSizeQueue.this.head, FixedSizeQueue.this.elements, (FixedSizeQueue.this.head + 1) % FixedSizeQueue.this.capacity, this.f5884d);
                FixedSizeQueue fixedSizeQueue = FixedSizeQueue.this;
                fixedSizeQueue.head = (fixedSizeQueue.head + 1) % FixedSizeQueue.this.capacity;
                FixedSizeQueue.access$510(FixedSizeQueue.this);
            } else {
                System.arraycopy(FixedSizeQueue.this.elements, i + 1, FixedSizeQueue.this.elements, i, (FixedSizeQueue.this.size - this.f5884d) - 1);
                FixedSizeQueue.this.tail = (r0.tail - 1) % FixedSizeQueue.this.capacity;
                FixedSizeQueue.access$510(FixedSizeQueue.this);
            }
            int i2 = this.f5884d;
            int i3 = this.f5883c;
            if (i2 < i3) {
                this.f5883c = i3 - 1;
            }
            this.f5884d = -1;
        }
    }

    public FixedSizeQueue(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i);
        }
        this.elements = new Object[i];
        this.capacity = i;
        this.head = 0;
        this.tail = (0 - 1) % i;
        this.size = 0;
    }

    static /* synthetic */ int access$510(FixedSizeQueue fixedSizeQueue) {
        int i = fixedSizeQueue.size;
        fixedSizeQueue.size = i - 1;
        return i;
    }

    public static void main(String[] strArr) {
        FixedSizeQueue fixedSizeQueue = new FixedSizeQueue(10);
        for (int i = 0; i < 35; i++) {
            fixedSizeQueue.add(Integer.valueOf(i));
        }
        System.out.println(fixedSizeQueue);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 32; i2 < 35; i2++) {
            arrayList.add(Integer.valueOf(i2));
        }
        fixedSizeQueue.retainAll(arrayList);
        System.out.println(fixedSizeQueue);
        System.out.println(fixedSizeQueue.contains(31));
    }

    @Override // java.util.Queue, java.util.Collection
    public boolean add(E e) {
        this.modCount++;
        int i = this.tail + 1;
        int i2 = this.capacity;
        int i3 = i % i2;
        this.tail = i3;
        this.elements[i3] = e;
        int i4 = this.size;
        if (i4 + 1 <= i2) {
            i2 = i4 + 1;
        }
        this.size = i2;
        int i5 = this.tail + 1;
        int i6 = this.capacity;
        this.head = ((i5 + i6) - i2) % i6;
        return true;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
        return true;
    }

    @Override // java.util.Collection
    public void clear() {
        this.modCount++;
        this.size = 0;
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        if (obj == null) {
            for (Object obj2 : this.elements) {
                if (obj2 == null) {
                    return true;
                }
            }
        } else {
            for (Object obj3 : this.elements) {
                if (obj.equals(obj3)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        if (collection.size() > this.size) {
            return false;
        }
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Queue
    public E element() {
        if (this.size != 0) {
            return (E) this.elements[this.head];
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isFull() {
        return this.capacity == this.size;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new Iter();
    }

    @Override // java.util.Queue
    public boolean offer(E e) {
        return add(e);
    }

    @Override // java.util.Queue
    public E peek() {
        if (this.size == 0) {
            return null;
        }
        return (E) this.elements[this.head];
    }

    @Override // java.util.Queue
    public E poll() {
        this.modCount++;
        int i = this.size;
        if (i == 0) {
            return null;
        }
        Object[] objArr = this.elements;
        int i2 = this.head;
        E e = (E) objArr[i2];
        this.head = (i2 + 1) % this.capacity;
        this.size = i - 1;
        return e;
    }

    @Override // java.util.Queue
    public E remove() {
        this.modCount++;
        int i = this.size;
        if (i == 0) {
            throw new NoSuchElementException();
        }
        Object[] objArr = this.elements;
        int i2 = this.head;
        E e = (E) objArr[i2];
        this.head = (i2 + 1) % this.capacity;
        this.size = i - 1;
        return e;
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        Iterator<E> it = iterator();
        int i = 0;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                i++;
            }
        }
        this.modCount += i;
        return i != 0;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        Iterator<E> it = iterator();
        int i = 0;
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                i++;
            }
        }
        this.modCount += i;
        return i != 0;
    }

    @Override // java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        Object[] objArr = new Object[this.size];
        int i = this.head;
        int i2 = 0;
        while (i2 < this.size) {
            objArr[i2] = this.elements[i];
            i2++;
            i = (i + 1) % this.capacity;
        }
        return objArr;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i = this.head;
        for (int i2 = 0; i2 < this.size; i2++) {
            if (i2 != 0) {
                sb.append(',');
                sb.append(' ');
            }
            sb.append(this.elements[i]);
            i = (i + 1) % this.capacity;
        }
        sb.append(']');
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int i;
        int i2 = 0;
        if (tArr.length < this.size) {
            T[] tArr2 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.size));
            int i3 = this.head;
            while (i2 < this.size) {
                tArr2[i2] = this.elements[i3];
                i2++;
                i3 = (i3 + 1) % this.capacity;
            }
            return tArr2;
        }
        int i4 = this.head;
        while (true) {
            i = this.size;
            if (i2 >= i) {
                break;
            }
            tArr[i2] = this.elements[i4];
            i2++;
            i4 = (i4 + 1) % this.capacity;
        }
        if (tArr.length > i) {
            tArr[i] = 0;
        }
        return tArr;
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        this.modCount++;
        if (obj == null) {
            int i = this.head;
            int i2 = 0;
            while (true) {
                int i3 = this.size;
                if (i2 >= i3) {
                    break;
                }
                Object[] objArr = this.elements;
                if (objArr[i] == null) {
                    int i4 = this.head;
                    if (i >= i4) {
                        System.arraycopy(objArr, i4, objArr, (i4 + 1) % this.capacity, i2);
                        this.head = (this.head + 1) % this.capacity;
                        this.size--;
                    } else {
                        System.arraycopy(objArr, i + 1, objArr, i, (i3 - i2) - 1);
                        this.tail = (this.tail - 1) % this.capacity;
                        this.size--;
                    }
                    return true;
                }
                i2++;
                i = (i + 1) % this.capacity;
            }
        } else {
            int i5 = this.head;
            int i6 = 0;
            while (i6 < this.size) {
                if (obj.equals(this.elements[i5])) {
                    int i7 = this.head;
                    if (i5 >= i7) {
                        Object[] objArr2 = this.elements;
                        System.arraycopy(objArr2, i7, objArr2, (i7 + 1) % this.capacity, i6);
                        this.head = (this.head + 1) % this.capacity;
                        this.size--;
                    } else {
                        Object[] objArr3 = this.elements;
                        System.arraycopy(objArr3, i5 + 1, objArr3, i5, (this.size - i6) - 1);
                        this.tail = (this.tail - 1) % this.capacity;
                        this.size--;
                    }
                    return true;
                }
                i6++;
                i5 = (i5 + 1) % this.capacity;
            }
        }
        return false;
    }
}