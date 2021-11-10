package main.linear;

import main.*;
import main.util.*;
import java.util.*;
import java.util.function.*;

public class CircularArray<E> extends LinearStructure<E> {

    protected final int capacity;
    protected int size;
    transient Object[] elementData;

    public CircularArray() {
        this.capacity = 0;
        this.size = 0;
        this.elementData = new Object[0];
    }

    public CircularArray(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        if (capacity >= 0) {
            this.elementData = new Object[capacity];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
    }

    public CircularArray(LinearStructure<E> c) {
        this(c.size());
        addAll(c);
    }

    @Override
    public boolean add(E e) {
        try {
            set(indexOf(null), e);
            size++;
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public boolean addAll(LinearStructure<E> c) {
        if (capacity - size < c.size()) {
            return false;
        }
        for (E e : (Iterable<E>) c) {
            add(e);
        }
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            remove(i);
        }
    }

    @Override
    public CircularArray<E> clone() {
        return new CircularArray<>(this);
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public int compare(Structure o1, Structure o2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CircularArray)) {
            return false;
        }
        return compare(this, (CircularArray<E>) o) == 0;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    public Iterator<E> iterator(int index) {
        return new Itr(index);
    }

    @Override
    public boolean remove(Object o) {
        try {
            remove(indexOf(o));
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean removeAll(LinearStructure<E> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public boolean replaceAll(LinearStructure<E> c, E replacement) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(LinearStructure<E> c) {
        for (int i = 0; i < capacity; i++) {
            if (!contains(get(i))) {
                remove(i);
                i--;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> reverseIterator() {
        return new Itr(true);
    }

    public Iterator<E> reverseIterator(int index) {
        return new Itr(index, true);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return main.Arrays.toString(elementData);
    }

    @Override
    public boolean add(int index, E element) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean addAll(int index, IndexBased<E> c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elementData[index % capacity];
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < capacity; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < capacity; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = capacity - 1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = capacity - 1; i >= 0; i--) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public E remove(int index) {
        E oldValue = set(index % capacity, null);
        size--;
        return oldValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        E oldValue = (E) elementData[index % capacity];
        elementData[index % capacity] = element;
        return oldValue;
    }

    @Override
    public Object[] toArray() {
        return subArray(0, capacity - 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        a = (T[]) subArray(0, capacity - 1);
        return a;
    }
    
    // helper methods
    public int capacity() {
        return capacity;
    }

    @SuppressWarnings("unchecked")
    private E[] subArray(int fromIndex, int toIndex) {
        Object[] subarray = new Object[toIndex - fromIndex + 1];
        System.arraycopy(elementData, fromIndex, subarray, 0, subarray.length);
        return (E[]) subarray;
    }

    public void shiftRight() {
        @SuppressWarnings("unchecked")
        E lastValue = (E) elementData[capacity - 1];
        for (int i = capacity - 1; i > 0; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[0] = lastValue;
    }

    public void shiftRight(int times) {
        while (times > 0) {
            shiftRight();
            times--;
        }
    }

    public void shiftLeft() {
        @SuppressWarnings("unchecked")
        E firstValue = (E) elementData[0];
        for (int i = 1; i < capacity; i++) {
            elementData[i - 1] = elementData[i];
        }
        elementData[capacity - 1] = firstValue;
    }

    public void shiftLeft(int times) {
        while (times > 0) {
            shiftLeft();
            times--;
        }
    }

    private class Itr implements BidirectionalIterator<E> {
        int cursor;
        int lastRet;
        int finalIndex;
        private boolean reverse;
        private boolean looped;

        Itr() {
            this(0);
        }

        Itr(boolean reverse) {
            this(reverse ? capacity() : 0, reverse);
        }

        Itr(int index) {
            this(index, false);
        }

        Itr(int index, boolean reverse) {
            this.reverse = reverse;
            cursor = this.reverse ? index + 1 : index - 1;
            lastRet = this.reverse ? cursor + 1 : cursor - 1;
            finalIndex = cursor;
            looped = false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int capacity = CircularArray.this.capacity;
            int i = cursor;
            if (i < capacity) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < capacity; i++)
                    action.accept((E) es[i]);

                cursor = i;
                lastRet = i - 1;
            }
        }

        @Override
        public boolean hasNext() {
            return !looped;
        }

        @Override
        public boolean hasPrevious() {
            return !looped;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (reverse) {
                return previous();
            } else {
                if (!hasNext())
                    throw new NoSuchElementException();

                E nex = (E) elementData[(cursor = nextIndex())];
                return nex;
            }
        }

        @Override
        public int nextIndex() {
            int newIndex = (cursor == capacity - 1) ? 0 : cursor + 1;
            if (newIndex == finalIndex) {
                looped = true;
            }
            return newIndex;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            E prev = (E) elementData[(cursor = previousIndex())];
            return prev;
        }

        @Override
        public int previousIndex() {
            int newIndex = (cursor == 0) ? capacity - 1 : cursor - 1;
            if (newIndex == finalIndex) {
                looped = true;
            }
            return newIndex;
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                CircularArray.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}