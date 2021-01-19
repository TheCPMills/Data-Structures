package main.linear;

import main.*;
import main.util.*;
import java.util.*;
import java.util.function.*;

public class SinglyLinkedList<E> extends LinearStructure<E> implements IndexBased<E> {
    transient int size = 0;
    transient Node<E> head;
    transient Node<E> tail;
    protected transient int modCount = 0;

    public SinglyLinkedList() {
    }

    public SinglyLinkedList(Structure c) {
        this();
        addAll(c);
    }

    @Override
    public boolean add(E e) {
        add(size, e);
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
        }
        head = tail = null;
        size = 0;
        modCount++;
    }

    @Override
    public Object clone() {
        SinglyLinkedList<E> clone = superClone();

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
                o1ParamClass = ((SinglyLinkedList<E>) (o1)).get(0).getClass();
                o2ParamClass = ((SinglyLinkedList<E>) (o2)).get(0).getClass();
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
                    if (!(((SinglyLinkedList<E>) (o1)).get(0) instanceof Comparable)) {
                        String s = ((SinglyLinkedList<E>) (o1)).get(0).getClass().getName();
                        throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
                    }
                } else if (o2Size != 0) {
                    if (!(((SinglyLinkedList<E>) (o2)).get(0) instanceof Comparable)) {
                        String s = ((SinglyLinkedList<E>) (o2)).get(0).getClass().getName();
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
        if (!(o instanceof SinglyLinkedList)) {
            return false;
        }
        return compare(this, (SinglyLinkedList<E>) o) == 0;
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
            linkBetween(element, (index == 0) ? null : node(index - 1));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Structure c) {
        checkPositionIndex(index);

        Object[] a = c.arrayify();

        if (size == 0 || index == size) {
            for (Object o : a) {
                add((E) o);
            }
        } else {
            for (Object o : a) {
                add(index++, (E) o);
            }
        }
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
        int index = -1;
        int i = 0;
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.getNext(), i++) {
                if (x.getItem() == null)
                    index = i;
            }
        } else {
            for (Node<E> x = head; x != null; x = x.getNext(), i++) {
                if (o.equals(x.getItem()))
                    index = i;
            }
        }
        return index;
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
        Node<E> x = head;
        for (int i = 0; i < index; i++)
            x = x.getNext();
        return x;
    }

    void linkToTail(E e) {
        final Node<E> newNode = new Node<>(e, null);

        if (head == null)
            head = newNode;
        else
            tail.setNext(newNode);
        size++;
        tail = newNode;
        modCount++;
    }

    void linkBetween(E e, Node<E> pred) {
        final Node<E> newNode;
        if (pred == null) {
            newNode = new Node<>(e, head);
            head = newNode;
        } else {
            newNode = new Node<>(e, pred.getNext());
            pred.setNext(newNode);
        }
        size++;
        tail = node(size - 1);
        modCount++;
    }

    E unlink(Node<E> x) {
        final E element = x.getItem();
        Node<E> succ = x.getNext();
        Node<E> pred = head;

        if (x.equals(head)) {
            head = succ;
        } else {
            while (pred.getNext() != x) {
                pred = pred.getNext();
            }
            pred.setNext(succ);
        }

        if (succ != null) {
            x.setNext(null);
        }

        x.setItem(null);
        size--;
        tail = node(size - 1);
        modCount++;
        return element;
    }

    @SuppressWarnings("unchecked")
    private SinglyLinkedList<E> superClone() {
        return (SinglyLinkedList<E>) super.clone();
    }

    private static class Node<E> {
        private E item;
        private Node<E> next;

        public Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if(this == null || o == null) {
                return this == null && o == null;
            } else if(this.next == null || ((Node<E>) (o)).next == null) {
                return item.equals(((Node<E>) (o)).item);
            } else {
                boolean itemsEqual = item.equals(((Node<E>) (o)).item);
                boolean nextEqual = next.equals(((Node<E>) (o)).next);
                return itemsEqual && nextEqual;
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

            lastReturned = next = (next == null) ? tail : node(previousIndex());
            nextIndex--;
            return lastReturned.getItem();
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
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