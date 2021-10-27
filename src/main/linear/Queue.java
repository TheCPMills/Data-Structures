package main.linear;

public class Queue<E> extends SinglyLinkedList<E> {
    public Queue() {
        super();
    }

    public Queue(LinearStructure<E> c) {
        super(c);
    }

    @Override
    public Queue<E> clone() {
        return new Queue<>(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Queue)) {
            return false;
        }
        return compare(this, (Queue<E>) o) == 0;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public boolean offer(E e) {
        return add(e);
    }

    public boolean offerAll(LinearStructure<E> c) {
        return addAll(c);
    }

    public E peek() {
        return get(size - 1);

    }
    
    public E poll() {
        return remove(size - 1);
    }
}