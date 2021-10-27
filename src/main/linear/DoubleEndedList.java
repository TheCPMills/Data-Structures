package main.linear;

public class DoubleEndedList<E> extends LinkedList<E> {
    public DoubleEndedList() {
        super();
    }

    public DoubleEndedList(LinearStructure<E> c) {
        super(c);
    }
    
    public boolean addFirst(E e) {
        add(0, e);
        return true;
    }

    public boolean addLast(E e) {
        return add(e);
    }

    @Override
    public DoubleEndedList<E> clone() {
        return new DoubleEndedList<>(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DoubleEndedList)) {
            return false;
        }
        return compare(this, (DoubleEndedList<E>) o) == 0;
    }

    public E getFirst() {
        return get(0);
    }

    public E getLast() {
        return get(size - 1);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        return remove(size - 1);
    }
}

