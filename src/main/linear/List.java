package main.linear;

import main.*;
import main.util.*;
import main.util.Cloneable;
import java.util.*;
import java.util.function.*;

/**
 * A list of elements of type E.
 * @param <E> the type of the elements in this list
 */
public class List<E> extends LinearStructure<E> {
    /* The default initial capacity of a list */
    private static final int DEFAULT_CAPACITY = 10;
    transient Object[] elementData;
    private int size;
    protected transient int modCount = 0;

    public List() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    public List(LinearStructure<E> c) {
        this();
        addAll(c);
    }

    @Override
    public boolean add(E e) {
        modCount++;
        if (size == elementData.length)
            elementData = grow();
        elementData[size] = e;
        size++;
        return true;
    }

    @Override
    public boolean addAll(LinearStructure<E> c) {
        Object[] a = c.arrayify();
        modCount++;
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + numNew);
        System.arraycopy(a, 0, elementData, s, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public void clear() {
        modCount++;
        final Object[] es = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    public List<E> clone() {
        return new List<>(this);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int compare(Structure o1, Structure o2) {
        if (o1.getClass() != o2.getClass()) {
            throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
        } else {
            int o1Size = o1.size();
            int o2Size = o2.size();

            Class<?> o1ParamClass = (o1Size > 0) ? ((List<E>) (o1)).get(0).getClass() : null;
            Class<?> o2ParamClass = (o2Size > 0) ? ((List<E>) (o2)).get(0).getClass() : null;

            if (o1Size != 0 && o2Size != 0) {
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
                    if (Utilities.isRelatedTo(o1ParamClass, Cloneable.class)) {
                        obj = (Comparator<Object>) ((Cloneable) ((List<E>) (o1)).get(0)).clone();
                    } else {
                        obj = (Comparator<Object>) ((List<E>) (o1)).get(0);
                    }
                }

                int result;
                Iterator<E> i1 = o1.iterator();
                Iterator<E> i2 = o2.iterator();
                int smallerSize = Math.min(o1Size, o2Size);
                for (int i = 0; i < smallerSize; i++) {
                    E obj1 = i1.next();
                    E obj2 = i2.next();
                    result = obj.compare(obj1, obj2);
                    if (result != 0) {
                        return result;
                    }
                }
                return Integer.compare(o1Size, o2Size);
            } else if (Utilities.isRelatedTo(o1ParamClass, Comparable.class)) {
                if (o1Size != 0) {
                    if (!(((List<E>) (o1)).get(0) instanceof Comparable)) {
                        String s = ((List<E>) (o1)).get(0).getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                } else if (o2Size != 0) {
                    if (!(((List<E>) (o2)).get(0) instanceof Comparable)) {
                        String s = ((List<E>) (o2)).get(0).getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                }

                int result;
                Comparable<E> obj1;
                Iterator<E> i1 = o1.iterator();
                Iterator<E> i2 = o2.iterator();
                int smallerSize = Math.min(o1Size, o2Size);
                for (int i = 0; i < smallerSize; i++) {
                    obj1 = (Comparable<E>) i1.next();
                    E obj2 = i2.next();
                    result = obj1.compareTo(obj2);
                    if (result != 0) {
                        return result;
                    }
                }
                return Integer.compare(o1Size, o2Size);
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
        if (!(o instanceof List)) {
            return false;
        }
        return compare(this, (List<E>) o) == 0;
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
        final Object[] es = elementData;
        final int size = this.size;
        int i = 0;
        found: {
            if (o == null) {
                for (; i < size; i++)
                    if (es[i] == null)
                        break found;
            } else {
                for (; i < size; i++)
                    if (o.equals(es[i]))
                        break found;
            }
            return false;
        }
        fastRemove(es, i);
        return true;
    }

    @Override
    public boolean removeAll(LinearStructure<E> c) {
        return batchRemove(c, false, 0, size);
    }

    @Override
    public boolean replaceAll(LinearStructure<E> c, E replacement) {
        return batchReplace(c, replacement, 0, size);
    }

    @Override
    public boolean retainAll(LinearStructure<E> c) {
        return batchRemove(c, true, 0, size);
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
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    @Override
    public boolean add(int index, E element) {
        rangeCheckForAdd(index);
        modCount++;
        final int s;
        Object[] elementData;
        if ((s = size) == (elementData = this.elementData).length)
            elementData = grow();
        System.arraycopy(elementData, index, elementData, index + 1, s - index);
        elementData[index] = element;
        size = s + 1;
        return true;
    }

    @Override
    public boolean addAll(int index, IndexBased<E> c) {
        rangeCheckForAdd(index);
        Object[] a = c.arrayify();
        modCount++;
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + numNew);

        int numMoved = s - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        System.arraycopy(a, 0, elementData, index, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    @Override
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o, 0, size);
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;

        @SuppressWarnings("unchecked")
        E oldValue = (E) es[index];
        fastRemove(es, index);

        return oldValue;
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    @Override
    public Object[] toArray() {
        return main.Arrays.copyOf(elementData, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) main.Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // helper methods
    // private void trimToSize() {
    //     modCount++;
    //     if (size < elementData.length) {
    //         elementData = (size == 0) ? new Object[0] : main.Arrays.copyOf(elementData, size);
    //     }
    // }

    // private void ensureCapacity(int minCapacity) {
    //     if (minCapacity > elementData.length
    //             && !(defaultEmpty() && minCapacity <= DEFAULT_CAPACITY)) {
    //         modCount++;
    //         grow(minCapacity);
    //     }
    // }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || !defaultEmpty()) {
            int newCapacity = newLength(oldCapacity, minCapacity - oldCapacity, 
                    oldCapacity >> 1);
            return elementData = main.Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    private boolean defaultEmpty() {
        boolean empty = isEmpty();
        boolean defaultSize = (size == DEFAULT_CAPACITY);
        return empty && defaultSize;
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) {
        return (E) elementData[index];
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private void fastRemove(Object[] es, int i) {
        modCount++;
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    @SuppressWarnings("unchecked")
    boolean batchRemove(Structure<E> c, boolean complement, final int from, final int end) {
        Objects.requireNonNull(c);
        final Object[] es = elementData;
        int r;
        // Optimize for initial run of survivors
        for (r = from;; r++) {
            if (r == end)
                return false;
            if (((IndexBased<E>) (c)).contains((E) es[r]) != complement)
                break;
        }
        int w = r++;
        try {
            for (E e; r < end; r++)
                if (((IndexBased<E>) (c)).contains(e = (E) es[r]) == complement)
                    es[w++] = e;
        } catch (Throwable ex) {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            System.arraycopy(es, r, es, w, end - r);
            w += end - r;
            throw ex;
        } finally {
            modCount += end - w;
            shiftTailOverGap(es, w, end);
        }
        return true;
    }

    @SuppressWarnings({"unchecked", "unused"})
    boolean batchReplace(Structure<E> c, E replacement, final int from, final int end) {
        Objects.requireNonNull(c);
        // Optimize for initial run of survivors
        final Object[] es = elementData;
        int r;
        for (r = from;; r++) {
            if (r == end)
                return false;
            if (((IndexBased<E>) (c)).contains((E) es[r]))
                break;
        }
        // Replace all elements in [from, end) that match c
        int w = r++;
        try {
            for (E e; r < end; r++)
                if (((IndexBased<E>) (c)).contains(e = (E) es[r]))
                    es[w++] = replacement;
        } catch (Throwable ex) {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            System.arraycopy(es, r, es, w, end - r);
            w += end - r;
            throw ex;
        } finally {
            modCount += end - w;
            shiftTailOverGap(es, w, end);
        }
        return true;
    }

    int indexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    int lastIndexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        // assert oldLength >= 0
        // assert minGrowth > 0

        int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
        if (newLength - Integer.MAX_VALUE - 8 <= 0) {
            return newLength;
        }
        return hugeLength(oldLength, minGrowth);
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError("Required array length too large");
        }
        if (minLength <= Integer.MAX_VALUE - 8) {
            return Integer.MAX_VALUE - 8;
        }
        return Integer.MAX_VALUE;
    }

    private void shiftTailOverGap(Object[] es, int lo, int hi) {
        System.arraycopy(es, hi, es, lo, size - hi);
        for (int to = size, i = (size -= hi - lo); i < to; i++)
            es[i] = null;
    }

    private class Itr implements BidirectionalIterator<E> {
        int cursor;
        int lastRet;
        private boolean reverse;
        int expectedModCount = modCount;

        Itr() {
            this(0);
        }

        Itr(boolean reverse) {
            this(reverse ? size() : 0, reverse);
        }

        Itr(int index) {
            this(index, false);
        }

        Itr(int index, boolean reverse) {
            this.reverse = reverse;
            cursor = this.reverse ? index + 1 : index - 1;
            lastRet = this.reverse ? -1 : size();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = List.this.size;
            int i = cursor;
            if (!reverse && i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept((E) es[i]);

                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
            if (reverse && (i -= 2) > -1) {
                final Object[] es = elementData;
                if (i <= -1)
                    throw new ConcurrentModificationException();
                for (; i > -1 && modCount == expectedModCount; i--)
                    action.accept((E) es[i]);

                cursor = i;
                lastRet = i + 1;
                checkForComodification();
            }
        }

        @Override
        public boolean hasNext() {
            return reverse ? hasPrevious() : cursor != size - 1;
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
                checkForComodification();
                if (!hasNext())
                    throw new NoSuchElementException();
                return (E) elementData[cursor = nextIndex()];
            }
        }

        @Override
        public int nextIndex() {
            return cursor + 1;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            E prev = (E) elementData[cursor = previousIndex()];
            return prev;
            // int i = cursor - 1;
            // if (i < 0)
            //     throw new NoSuchElementException();
            // Object[] elementData = List.this.elementData;
            // if (i >= elementData.length)
            //     throw new ConcurrentModificationException();
            // cursor = i;
            // return (E) elementData[lastRet = i];
        }

        @Override
        public int previousIndex() {
            return reverse ? cursor - 1 : cursor + 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                List.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}