package main.linear;

import main.*;

public class Multiset<E> extends List<E> {
    public Multiset() {
        super();
    }

    public Multiset(LinearStructure<E> c) {
        super(c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Multiset)) {
            return false;
        }
        Multiset<E> s1 = (Multiset<E>) clone();
        Multiset<E> s2 = (Multiset<E>) ((Structure<E>) (o)).clone();
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
}