package main.linear;

import main.*;
import main.util.*;
import java.util.*;
import java.util.function.*;

public class Array<E> extends LinearStructure<E> implements IndexBased<E> {

    protected final int capacity;
    protected int size;
    transient Object[] elementData;

    public Array() {
        this.capacity = 0;
        this.size = 0;
        this.elementData = new Object[0];
    }

    public Array(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        if (capacity >= 0) {
            this.elementData = new Object[capacity];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
    }

    public Array(Structure c) {
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
    @SuppressWarnings("unchecked")
    public boolean addAll(Structure c) {
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
    public Object clone() {
        Array<?> v = (Array<?>) super.clone();
        v.elementData = main.Arrays.copyOf(elementData, size);
        return v;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Structure o1, Structure o2) {
        if (o1.getClass() != o2.getClass()) {
            throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
        } else {
            int o1Capacity = ((Array<E>) (o1)).capacity();
            int o2Capacity = ((Array<E>) (o2)).capacity();

            int o1Size = o1.size();
            int o2Size = o2.size();

            Class<?> o1ParamClass = null;
            Class<?> o2ParamClass = null;

            if (o1Size != 0 && o2Size != 0) {
                o1ParamClass = ((LinearStructure<E>) (o1)).getIf(x -> x != null).arrayify()[0].getClass();
                o2ParamClass = ((LinearStructure<E>) (o2)).getIf(x -> x != null).arrayify()[0].getClass();
                if (o1ParamClass != o2ParamClass) {
                    throw new IllegalArgumentException(o1ParamClass + " cannot be compared to " + o2ParamClass);
                }
            } else if (o1Size == 0 && o2Size == 0) {
                return 0;
            }

            if (Utilities.isRelatedTo(o1ParamClass, Comparator.class)) {
                Comparator<Object> obj;
                try {
                    obj = (Comparator<Object>) o1ParamClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    obj = (Comparator<Object>) ((Structure) (o1)).clone();
                }

                int result;
                Iterator<E> i1 = o1.iterator();
                Iterator<E> i2 = o2.iterator();
                int smallerCapacity = Math.min(o1Capacity, o2Capacity);
                for (int i = 0; i < smallerCapacity; i++) {
                    E obj1 = i1.next();
                    E obj2 = i2.next();
                    result = obj.compare(obj1, obj2);
                    if (result != 0) {
                        return result;
                    }
                }
                return Integer.compare(o1Capacity, o2Capacity);
            } else if (Utilities.isRelatedTo(o1ParamClass, Comparable.class)) {
                if (o1Size != 0) {
                    if (!(((LinearStructure<E>) (o1)).getIf(x -> x != null).arrayify()[0] instanceof Comparable)) {
                        String s = ((LinearStructure<E>) (o1)).getIf(x -> x != null).arrayify()[0].getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                } else if (o2Size != 0) {
                    if (!(((LinearStructure<E>) (o2)).getIf(x -> x != null).arrayify()[0] instanceof Comparable)) {
                        String s = ((LinearStructure<E>) (o2)).getIf(x -> x != null).arrayify()[0].getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                }

                int result;
                Comparable<E> obj1;
                Iterator<E> i1 = o1.iterator();
                Iterator<E> i2 = o2.iterator();
                int smallerCapacity = Math.min(o1Capacity, o2Capacity);
                for (int i = 0; i < smallerCapacity; i++) {
                    obj1 = (Comparable<E>) i1.next();
                    E obj2 = i2.next();
                    result = obj1.compareTo(obj2);
                    if (result != 0) {
                        return result;
                    }
                }
                return Integer.compare(o1Capacity, o2Capacity);
            } else {
                String s = o1ParamClass.getName();
                throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Array)) {
            return false;
        }
        return compare(this, (Array<E>) o) == 0;
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
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public boolean removeAll(Structure c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Structure c) {
        for (int i = 0; i < capacity; i++) {
            if (!((IndexBased<E>) (c)).contains(get(i))) {
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
    public void add(int index, E element) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());

    }

    @Override
    public boolean addAll(int index, Structure c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elementData[index];
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
        E oldValue = set(index, null);
        size--;
        return oldValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        E oldValue = (E) elementData[index];
        elementData[index] = element;
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

    private class Itr implements BidirectionalIterator<E> {
        int cursor;
        int lastRet;
        private boolean reverse;

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
            lastRet = this.reverse ? -1 : capacity();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int capacity = Array.this.capacity;
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
            return reverse ? hasPrevious() : cursor != capacity - 1;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (reverse) {
                return previous();
            } else {
                if (!hasNext())
                    throw new NoSuchElementException();

                E nex = (E) elementData[cursor = nextIndex()];
                return nex;
            }
        }

        @Override
        public int nextIndex() {
            return cursor + 1;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            E prev = (E) elementData[cursor = previousIndex()];
            return prev;
        }

        @Override
        public int previousIndex() {
            return reverse ? cursor - 1 : cursor + 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                Array.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
