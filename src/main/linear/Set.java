package main.linear;

import main.*;

public class Set<E> extends List<E> {
    public Set() {
        super();
    }

    public Set(LinearStructure<E> c) {
        super(c);
    }
    
    @Override
    public boolean add(E e) {
        if (checkForDuplicate(e)) {
            return false;
        }
        return super.add(e);
    }

    @Override
    public boolean addAll(LinearStructure<E> c) {
        boolean allAdded = true;
        for (E e : (Iterable<E>) c) {
            if (!checkForDuplicate(e)) {
                add(e);
            } else {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<E> s1 = (Set<E>) clone();
        Set<E> s2 = (Set<E>) ((Structure<E>) (o)).clone();
        while (s1.size() != 0) {
            E elm = s1.remove(0);
            s2.remove(elm);
        }
        return s2.size() == 0;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean add(int index, E element) {
        if (!checkForDuplicate(element)) {
            return super.add(index, element);
        }
        return false;
    }

    @Override
    public boolean addAll(int index, IndexBased<E> c) {
        boolean allAdded = true;
        for(E e : (Iterable<E>) c) {
            if (!checkForDuplicate(e)) {
                add(index++, e);
            } else {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    public E set(int index, E element) {
        if (!checkForDuplicate(element)) {
            return super.set(index, element);
        }
        return get(index);
    }

    // helper methods
    private boolean checkForDuplicate(E e) {
        return contains(e);
    }
}