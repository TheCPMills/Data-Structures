package main;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public interface IndexBased<E> extends Structure {
    public boolean add(E e);

    public void add(int index, E element);

    public boolean addAll(int index, Structure c);

    public default boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public E get(int index);

    public int indexOf(Object o);

    public default int indexOf(Object o, int occurrence) {
        int occurrencesFromBack = occurrences(o) - occurrence;
        if (occurrencesFromBack < 0) {
            return -1;
        } else if (occurrence == 1) {
            return indexOf(o);
        } else {
            int index = lastIndexOf(o);
            while (occurrencesFromBack-- > 0)
                index = splice(0, index).lastIndexOf(o);
            return index;
        }
    }

    public default int[] indicesOf(Object o) {
        int[] indices = new int[occurrences(o)];
        int count = 0;
        int previousIndex = 0;
        int nextIndex = -1;
        IndexBased<E> subStruct;
        do {
            subStruct = splice(previousIndex, size());
            nextIndex = subStruct.indexOf(o);
            previousIndex += nextIndex + 1;
            indices[count++] = previousIndex - 1;
        } while (count < indices.length);
        return indices;
    }

    public int lastIndexOf(Object o);

    public default int occurrences(Object o) {
        int count = 0;
        int previousIndex = 0;
        int nextIndex = -1;
        IndexBased<E> subStruct;
        do {
            subStruct = splice(previousIndex, size());
            nextIndex = subStruct.indexOf(o);
            if (nextIndex != -1) {
                count++;
                previousIndex += nextIndex + 1;
            }
        } while (nextIndex != -1);
        return count;
    }

    // public default Object query(String fieldName) {
    //     Object o = this;
    //     Class<?> clazz = getClass();
    //     while (clazz != Object.class) {
    //         try {
    //             Object obj = null;
    //             obj = clazz.getDeclaredField(fieldName);
    //             return ((Field) (obj)).get(this);
    //         } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
    //             clazz = clazz.getSuperclass();
    //         }
    //     }
    //     throw new IllegalArgumentException("Could not find field \"" + fieldName + "\" in class " + getClass().getCanonicalName());
    // }

    public E remove(int index);
    
    public E set(int index, E element);

    public default boolean setAll(Structure c, E replacement) {
        for(Object o : c) {
            if(contains(o)) {
                set(indexOf(o), replacement);
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public default boolean setIf(Predicate<? super E> filter, E replacement) {
        Objects.requireNonNull(filter);
        boolean replaced = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            E element = each.next();
            if (filter.test(element)) {
                set(indexOf(element), replacement);
                replaced = true;
            }
        }
        return replaced;
    }
   
    @SuppressWarnings("unchecked")
    public default IndexBased<E> splice(int fromIndex, int toIndex) {
        IndexBased<E> splicedStruct;
        try {
            splicedStruct = (IndexBased<E>) getClass().getDeclaredConstructor().newInstance();
            for(; fromIndex < toIndex; fromIndex++) {
                splicedStruct.add(get(fromIndex));
            }
            return splicedStruct;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public default boolean swap(int element, int swappedElement) {
        E temp = get(element);
        set(element, get(swappedElement));
        set(swappedElement, temp);
        return true;
    }
}
