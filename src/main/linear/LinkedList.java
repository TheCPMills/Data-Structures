package main.linear;

import main.*;
import main.util.*;
import java.util.*;
import java.util.function.*;

public class LinkedList<E> extends LinearStructure<E> implements IndexBased<E> {
    transient int size = 0;
    transient Node<E> head;
    transient Node<E> tail;
    protected transient int modCount = 0;

    public LinkedList() {
    }

    public LinkedList(Structure c) {
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
        for (Node<E> x = head; x != null; x = x.getNext()) {
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
        LinkedList<E> clone = superClone();

        clone.head = clone.tail = null;
        clone.size = 0;
        clone.modCount = 0;

        for (Node<E> x = head; x != null; x = x.getNext())
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
                o1ParamClass = ((LinkedList<E>) (o1)).get(0).getClass();
                o2ParamClass = ((LinkedList<E>) (o2)).get(0).getClass();
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
                    if (!(((LinkedList<E>) (o1)).get(0) instanceof Comparable)) {
                        String s = ((LinkedList<E>) (o1)).get(0).getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                } else if (o2Size != 0) {
                    if (!(((LinkedList<E>) (o2)).get(0) instanceof Comparable)) {
                        String s = ((LinkedList<E>) (o2)).get(0).getClass().getName();
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
        if (!(o instanceof LinkedList)) {
            return false;
        }
        return compare(this, (LinkedList<E>) o) == 0;
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
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.getNext()) {
                if (x.getItem() == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = head; x != null; x = x.getNext()) {
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
    public void add(int index, E element) {
        checkPositionIndex(index);
        if (index == size)
            linkToTail(element);
        else
            linkBetween(element, node(index));
    }

    @Override
    public boolean addAll(int index, Structure c) {
        checkPositionIndex(index);

        Object[] a = c.arrayify();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        Node<E> pred, succ;
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
            Node<E> newNode = new Node<>(pred, e, null);
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

        size += numNew;
        modCount++;
        return true;
    }

    @Override
    public E get(int index) {
        checkElementIndex(index);
        return node(index).getItem();
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.getNext()) {
                if (x.getItem() == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = head; x != null; x = x.getNext()) {
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
            for (Node<E> x = tail; x != null; x = x.getPrevious()) {
                index--;
                if (x.getItem() == null)
                    return index;
            }
        } else {
            for (Node<E> x = tail; x != null; x = x.getPrevious()) {
                index--;
                if (o.equals(x.getItem()))
                    return index;
            }
        }
        return -1;
    }

    @Override
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.getItem();
        x.setItem(element);
        return oldVal;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.getNext())
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
        for (Node<E> x = head; x != null; x = x.getNext())
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

    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++)
                x = x.getNext();
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.getPrevious();
            return x;
        }
    }

    void linkToTail(E e) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(l, e, null);
        tail = newNode;
        if (l == null)
            head = newNode;
        else
            l.setNext(newNode);
        size++;
        modCount++;
    }

    void linkBetween(E e, Node<E> succ) {
        final Node<E> pred = succ.getPrevious();
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.setPrevious(newNode);
        if (pred == null)
            head = newNode;
        else
            pred.setNext(newNode);
        size++;
        modCount++;
    }

    E unlink(Node<E> x) {
        final E element = x.getItem();
        final Node<E> succ = x.getNext();
        final Node<E> pred = x.getPrevious();

        if (pred == null) {
            head = succ;
        } else {
            pred.setNext(succ);
            x.setPrevious(null);
        }

        if (succ == null) {
            tail = pred;
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
    private LinkedList<E> superClone() {
        return (LinkedList<E>) super.clone();
    }

    private static class Node<E> {
        private E item;
        private Node<E> next;
        private Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == null || o == null) {
                return this == null && o == null;
            } else if (this.next == null || ((Node<E>) (o)).next == null) {
                boolean itemsEqual = item.equals(((Node<E>) (o)).item);
                boolean nextEqual = next.equals(((Node<E>) (o)).next);
                return itemsEqual && nextEqual;
            } else if (this.prev == null || ((Node<E>) (o)).prev == null) {
                boolean itemsEqual = item.equals(((Node<E>) (o)).item);
                boolean prevEqual = prev.equals(((Node<E>) (o)).prev);
                return itemsEqual && prevEqual;
                
            } else {
                boolean itemsEqual = item.equals(((Node<E>) (o)).item);
                boolean nextEqual = next.equals(((Node<E>) (o)).next);
                boolean prevEqual = prev.equals(((Node<E>) (o)).prev);
                return itemsEqual && nextEqual && prevEqual;
            }
        }

        public E getItem() {
            return item;
        }

        public void setItem(E e) {
            item = e;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> n) {
            next = n;
        }

        public Node<E> getPrevious() {
            return prev;
        }

        public void setPrevious(Node<E> n) {
            prev = n;
        }

    }

    private class Itr implements BidirectionalIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;
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
            nextIndex = this.reverse ? index + 1 : index;
            next = node(nextIndex);
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
            return reverse ? hasPrevious() : nextIndex < size;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
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

            lastReturned = next = (next == null) ? tail : next.getPrevious();
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

            Node<E> lastNext = lastReturned.getNext();
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