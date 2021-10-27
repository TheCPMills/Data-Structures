package main.linear;

import main.*;

public class Stack<E> extends SinglyLinkedList<E> {
    public Stack() {
        super();
    }

    public Stack(LinearStructure<E> c) {
        super(c);
    }
    
    @Override
    public Stack<E> clone() {
        return new Stack<E>(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Stack)) {
            return false;
        }
        return compare(this, (Stack<E>) o) == 0;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public E peek() {
        return get(0);
    }

    public E pop() {
        return remove(0);
    }

    public boolean push(E e) {
        return add(e);
    }

    public boolean pushAll(LinearStructure<E> c) {
        return addAll(c);
    }

    // helper methods
    @Override
    public boolean add(E e) {
        add(0, e);
        return true;
    }

    @Override
    public boolean addAll(LinearStructure<E> c) {
        return addAll(0, c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Structure<E> c) {
        checkPositionIndex(index);

        Object[] a = c.arrayify();

        if (size == 0 || index == size) {
            for (Object o : a) {
                add((E) o);
            }
        } else {
            for (Object o : a) {
                add(index, (E) o);
            }
        }
        modCount++;
        return true;
    }
    
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
}