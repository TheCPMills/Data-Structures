package main.linear;

import main.*;
import main.util.*;
import java.util.*;
import java.util.function.*;

public class CircularList<E> extends LinearStructure<E> implements IndexBased<E> {
    transient int size = 0;
    transient CircularNode<E> head;
    transient CircularNode<E> tail;
    protected transient int modCount = 0;

    public CircularList() {
    }

    public CircularList(Structure c) {
        this();
        addAll(c);
    }

    @Override
    public boolean add(E e) {
        linkToTail(e);
        return true;
    }

    @Override
    public boolean addAll(Structure c) {
        return addAll(size, c);
    }

    @Override
    public void clear() {
        for (CircularNode<E> x = head; x != null; x = x.getNext()) {
            x.setItem(null);
            x.setNext(null);
            x.setPrevious(null);
        }
        head = tail = null;
        size = 0;
        modCount++;
    }

    @Override
    public Object clone() {
        CircularList<E> clone = superClone();

        clone.head = clone.tail = null;
        clone.size = 0;
        clone.modCount = 0;

        for (CircularNode<E> x = head; x != null; x = x.getNext())
            clone.add(x.getItem());

        return clone;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Structure o1, Structure o2) {
        if (o1.getClass() != o2.getClass()) {
            throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
        } else {
            int o1Size = o1.size();
            int o2Size = o2.size();

            Class<?> o1ParamClass = null;
            Class<?> o2ParamClass = null;

            if (o1Size != 0 && o2Size != 0) {
                o1ParamClass = ((CircularList<E>) (o1)).get(0).getClass();
                o2ParamClass = ((CircularList<E>) (o2)).get(0).getClass();
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
                    if (!(((CircularList<E>) (o1)).get(0) instanceof Comparable)) {
                        String s = ((CircularList<E>) (o1)).get(0).getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                } else if (o2Size != 0) {
                    if (!(((CircularList<E>) (o2)).get(0) instanceof Comparable)) {
                        String s = ((CircularList<E>) (o2)).get(0).getClass().getName();
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
        if (!(o instanceof CircularList)) {
            return false;
        }
        return compare(this, (CircularList<E>) o) == 0;
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
        return new Itr(index % size);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (CircularNode<E> x = head; x != null; x = x.getNext()) {
                if (x.getItem() == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (CircularNode<E> x = head; x != null; x = x.getNext()) {
                if (o.equals(x.getItem())) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
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
        for (int i = 0; i < size; i++) {
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
        return new Itr(index % size, true);
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
    public void add(int index, E element) {
        index = index % (size + 1);
        checkPositionIndex(index);
        if (index == size)
            linkToTail(element);
        else
            linkBetween(element, node(index));
    }

    @Override
    public boolean addAll(int index, Structure c) {
        index = index % (size + 1);
        checkPositionIndex(index);

        Object[] a = c.arrayify();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        CircularNode<E> pred, succ;
        if (index == size) {
            succ = null;
            pred = tail;
        } else {
            succ = node(index);
            pred = succ.getPrevious();
        }

        for (Object o : a) {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            CircularNode<E> newNode = new CircularNode<>(pred, e, null);
            if (pred == null)
                head = newNode;
            else
                pred.setNext(newNode);
            pred = newNode;
        }

        if (succ == null) {
            tail = pred;
        } else {
            pred.setNext(succ);
            succ.setPrevious(pred);
        }

        if (index == size) {
            tail.setNext(head);
            head.setPrevious(tail);
        } else if (index == 0) {
            head = tail.getNext();
        }

        size += numNew;
        modCount++;
        return true;
    }

    @Override
    public E get(int index) {
        index = index % size;
        checkElementIndex(index);
        return node(index).getItem();
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (CircularNode<E> x = head; x != null; x = x.getNext()) {
                if (x.getItem() == null)
                    return index;
                index++;
            }
        } else {
            for (CircularNode<E> x = head; x != null; x = x.getNext()) {
                if (o.equals(x.getItem()))
                    return index;
                index++;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (CircularNode<E> x = tail; x != null; x = x.getPrevious()) {
                index--;
                if (x.getItem() == null)
                    return index;
            }
        } else {
            for (CircularNode<E> x = tail; x != null; x = x.getPrevious()) {
                index--;
                if (o.equals(x.getItem()))
                    return index;
            }
        }
        return -1;
    }

    @Override
    public E remove(int index) {
        index = index % size;
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public E set(int index, E element) {
        index = index % (size + 1);
        checkElementIndex(index);
        CircularNode<E> x = node(index);
        E oldVal = x.getItem();
        x.setItem(element);
        return oldVal;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (CircularNode<E> x = head; x != null; x = x.getNext())
            result[i++] = x.getItem();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (CircularNode<E> x = head; x != null; x = x.getNext())
            result[i++] = x.getItem();

        if (a.length > size)
            a[size] = null;

        return a;
    }

    // helper methods
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    CircularNode<E> node(int index) {
        if (index < (size >> 1)) {
            CircularNode<E> x = head;
            for (int i = 0; i < index; i++)
                x = x.getNext();
            return x;
        } else {
            CircularNode<E> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.getPrevious();
            return x;
        }
    }

    void linkToTail(E e) {
        final CircularNode<E> l = tail;
        final CircularNode<E> f = head;
        final CircularNode<E> newNode = new CircularNode<>(l, e, f);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.setNext(newNode);
        }
        if (f == null) {
            tail = newNode;
        } else {
            f.setPrevious(newNode);
        }
        tail.setNext(head);
        head.setPrevious(tail);
        size++;
        modCount++;
    }

    void linkBetween(E e, CircularNode<E> succ) {
        final CircularNode<E> pred = succ.getPrevious();
        final CircularNode<E> newNode = new CircularNode<>(pred, e, succ);
        succ.setPrevious(newNode);
        if (pred == tail)
            head = newNode;
        else
            pred.setNext(newNode);
        size++;
        modCount++;
    }

    E unlink(CircularNode<E> x) {
        final E element = x.getItem();
        final CircularNode<E> succ = x.getNext();
        final CircularNode<E> pred = x.getPrevious();

        if (pred == tail) {
            head = x.getNext();
            head.setPrevious(x.getPrevious());
        } else if (pred == tail.getPrevious()) {
            tail = x.getPrevious();
            tail.setNext(x.getNext());
        } else {
            pred.setNext(succ);
            x.setPrevious(null);
        }

        if (succ == head) {
            if (x == pred.getNext()) {
                tail = x.getPrevious();
                tail.setNext(x.getNext());
            } else {
                head = x.getNext();
                head.setPrevious(x.getPrevious());
            }
        } else {
            succ.setPrevious(pred);
            x.setNext(null);
        }

        x.setItem(null);

        size--;
        modCount++;
        return element;
    }

    @SuppressWarnings("unchecked")
    private CircularList<E> superClone() {
        return (CircularList<E>) super.clone();
    }

    private static class CircularNode<E> {
        private E item;
        private CircularNode<E> next;
        private CircularNode<E> prev;

        CircularNode(CircularNode<E> prev, E element, CircularNode<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == null || o == null) {
                return this == null && o == null;
            } else if (this.next == null || ((CircularNode<E>) (o)).next == null) {
                boolean itemsEqual = item.equals(((CircularNode<E>) (o)).item);
                boolean nextEqual = next.equals(((CircularNode<E>) (o)).next);
                return itemsEqual && nextEqual;
            } else if (this.prev == null || ((CircularNode<E>) (o)).prev == null) {
                boolean itemsEqual = item.equals(((CircularNode<E>) (o)).item);
                boolean prevEqual = prev.equals(((CircularNode<E>) (o)).prev);
                return itemsEqual && prevEqual;

            } else {
                boolean itemsEqual = item.equals(((CircularNode<E>) (o)).item);
                boolean nextEqual = next.equals(((CircularNode<E>) (o)).next);
                boolean prevEqual = prev.equals(((CircularNode<E>) (o)).prev);
                return itemsEqual && nextEqual && prevEqual;
            }
        }

        public E getItem() {
            return item;
        }

        public void setItem(E e) {
            item = e;
        }

        public CircularNode<E> getNext() {
            return next;
        }

        public void setNext(CircularNode<E> n) {
            next = n;
        }

        public CircularNode<E> getPrevious() {
            return prev;
        }

        public void setPrevious(CircularNode<E> n) {
            prev = n;
        }

        public String toString() {
            String s = "";
            s += item;
            s += (prev == null) ? ", Previous: null": ", Previous: " + prev.getItem();
            s += (next == null) ? ", Next: null" : ", Next: " + next.getItem();
            return s;
        }

    }

    private class Itr implements BidirectionalIterator<E> {
        private CircularNode<E> lastReturned;
        private CircularNode<E> next;
        private int nextIndex;
        private int finalIndex;
        private boolean reverse;
        private int expectedModCount = modCount;

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
            nextIndex = index;
            next = node(nextIndex);
            finalIndex = this.reverse ? index - size() : index + size();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size) {
                action.accept(next.getItem());
                lastReturned = next;
                next = next.getNext();
                nextIndex++;
            }
            checkForComodification();
        }

        @Override
        public boolean hasNext() {
            return nextIndex != finalIndex;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex != finalIndex;
        }

        @Override
        public E next() {
            if (reverse) {
                return previous();
            } else {
                checkForComodification();
                if (!hasNext())
                    throw new NoSuchElementException();

                lastReturned = next;
                next = next.getNext();
                nextIndex++;
                return lastReturned.getItem();
            }
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public E previous() {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.getPrevious();
            nextIndex--;
            return lastReturned.getItem();
        }

        @Override
        public int previousIndex() {
            return reverse ? nextIndex + 1 : nextIndex - 1;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();

            CircularNode<E> lastNext = lastReturned.getNext();
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}