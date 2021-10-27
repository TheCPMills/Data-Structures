package main.linear;

import main.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class LinearStructure<E> implements Structure<E> {
    public abstract boolean add(E e);

    public abstract boolean addAll(LinearStructure<E> c);

    public boolean addIf(Predicate<? super E> filter, LinearStructure<E> c) {
        Objects.requireNonNull(filter);
        boolean added = false;
        final Iterator<? extends E> each = c.iterator();
        while (each.hasNext()) {
            E element = each.next();
            if (filter.test(element)) {
                add(element);
                added = true;
            }
        }
        return added;
    }

    @Override
    public Object[] arrayify() {
        return toArray();
    }

    public abstract LinearStructure<E> clone();

    // public abstract boolean contains(Object o);

    @SuppressWarnings("unchecked")
    public boolean containsAll(LinearStructure<E> c) {
        for(Object o : c) {
            if(!((IndexBased<E>) (this)).contains(o)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public Structure<E> getAll(LinearStructure<E> c) {
        LinearStructure<E> retrieved;
        try {
            retrieved = (LinearStructure<E>) getClass().getDeclaredConstructor().newInstance();
            for (Object o : c) {
                if (((IndexBased<E>) (this)).contains(o)) {
                    retrieved.add((E) o);
                }
            }
            return retrieved;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public LinearStructure<E> getIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        LinearStructure<E> retrieved;
        try {
            if (this instanceof Array) {
                retrieved = (LinearStructure<E>) getClass().getDeclaredConstructor(int.class).newInstance(((Array<?>) this).capacity());
                } else {
                    retrieved = (LinearStructure<E>) getClass().getDeclaredConstructor().newInstance();
                }
                final Iterator<E> each = iterator();
                while (each.hasNext()) {
                    E element = each.next();
                    if (filter.test(element)) {
                    retrieved.add(element);
                }
            }
            return retrieved;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    public CircularList<E> loop() {
        return new CircularList<>(this);
    }

    // public abstract int occurrences(Object o);

    public abstract boolean remove(Object o);

    public abstract boolean removeAll(LinearStructure<E> c);

    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    public abstract boolean retainAll(LinearStructure<E> c);

    public boolean retainIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (!filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    public abstract boolean replaceAll(LinearStructure<E> c, E replacement);

    @SuppressWarnings("unchecked")
    public boolean replaceIf(Predicate<? super E> filter, E replacement) {
        Objects.requireNonNull(filter);
        boolean replaced = false;
        for (int i = 0; i < size(); i++) {
            if (filter.test(((IndexBased<E>) (this)).get(i))) {
                ((IndexBased<E>) (this)).set(i, replacement);
                replaced = true;
            }
        }
        return replaced;
    }
    
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public abstract Object[] toArray();

    public abstract <T> T[]toArray(T[] a);
}