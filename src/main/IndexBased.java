package main;

import main.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Resizable-array implementation of the {@code List} interface. Implements all
 * optional list operations, and permits all elements, including {@code null}.
 * In addition to implementing the {@code List} interface, this class provides
 * methods to manipulate the size of the array that is used internally to store
 * the list. (This class is roughly equivalent to {@code Vector}, except that it
 * is unsynchronized.)
 *
 * <p>
 * The {@code size}, {@code isEmpty}, {@code get}, {@code set},
 * {@code iterator}, and {@code listIterator} operations run in constant time.
 * The {@code add} operation runs in <i>amortized constant time</i>, that is,
 * adding n elements requires O(n) time. All of the other operations run in
 * linear time (roughly speaking). The constant factor is low compared to that
 * for the {@code LinkedList} implementation.
 *
 * <p>
 * Each {@code ArrayList} instance has a <i>capacity</i>. The capacity is the
 * size of the array used to store the elements in the list. It is always at
 * least as large as the list size. As elements are added to an ArrayList, its
 * capacity grows automatically. The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized time
 * cost.
 *
 * <p>
 * An application can increase the capacity of an {@code ArrayList} instance
 * before adding a large number of elements using the {@code ensureCapacity}
 * operation. This may reduce the amount of incremental reallocation.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access an {@code ArrayList} instance concurrently, and at
 * least one of the threads modifies the list structurally, it <i>must</i> be
 * synchronized externally. (A structural modification is any operation that
 * adds or deletes one or more elements, or explicitly resizes the backing
 * array; merely setting the value of an element is not a structural
 * modification.) This is typically accomplished by synchronizing on some object
 * that naturally encapsulates the list.
 *
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList} method.
 * This is best done at creation time, to prevent accidental unsynchronized
 * access to the list:
 * 
 * <pre>
 *   List list = Collections.synchronizedList(new ArrayList(...));
 * </pre>
 *
 * <p id="fail-fast">
 * The iterators returned by this class's {@link #iterator() iterator} and
 * {@link #listIterator(int) listIterator} methods are <em>fail-fast</em>: if
 * the list is structurally modified at any time after the iterator is created,
 * in any way except through the iterator's own {@link ListIterator#remove()
 * remove} or {@link ListIterator#add(Object) add} methods, the iterator will
 * throw a {@link ConcurrentModificationException}. Thus, in the face of
 * concurrent modification, the iterator fails quickly and cleanly, rather than
 * risking arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 *
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it
 * is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification. Fail-fast iterators throw
 * {@code ConcurrentModificationException} on a best-effort basis. Therefore, it
 * would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to
 * detect bugs.</i>
 *
 * <p>
 * This class is a member of the <a href="
 * {@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements in this list
 *
 * @author Josh Bloch
 * @author Neal Gafter
 * @version 1.0.0 (27 October 2021)
 * @see Collection
 * @see List
 * @see LinkedList
 * @see Vector
 * @see main.linear.LinearStructure
 * @since 1.2
 */
public interface IndexBased<E> extends Structure<E> {
    /***************************/
    /* MODIFICATION OPERATIONS */
    /***************************/

    /**
     * Appends the specified element to the end of this list.
     * @param element element to be appended to this list
     * @return {@code true}
     * @since 1.0.0
     */
    
    public boolean add(E element);

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted
     * @param element element to be appended to this list
     * @return {@code true}
     * @since 1.0.0
     */
    public boolean add(int index, E element);

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0} or {@code index >= size()})
     * @since 1.0.0
     */
    public E remove(int index);

    /**
     * Removes the first occurrence of the specified element from this list, if
     * it is present. If the list does not contain the element, it is unchanged.
     * More formally, removes the element with the lowest index {@code i} such
     * that {@code Objects.equals(o, get(i))} (if such an element exists).
     * @param element element to be removed from this list, if present
     * @return {@code true} if the list contained the specified element
     * @since 1.0.0
     */
    public abstract boolean remove(E element);

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0} or {@code index >= size()})
     * @since 1.0.0
     */
    public E set(int index, E element);

    /********************/
    /* QUERY OPERATIONS */
    /********************/

    /**
     * Determine if this list contains the specified element.
     * @param element element whose presence in this list is to be tested
     * @return {@code true} if the specified element is present in this list, {@code false} otherwise
     */
    public default boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    /**
     * Returns the element at the specified position in this list.
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0} or {@code index >= size()})
     * @since 1.0.0
     */
    public E get(int index);

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     * @param element element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     * @since 1.0.0
     * @see #lastIndexOf(E)
     * @see #indexOf(E, int)
     * @see #lastIndexOf(E, int)
     * @see #contains(E)
     * @see #contains(E, int)
     */
    public int indexOf(E element);

    /**
     * Returns the index of the nth occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     * @param element element to search for
     * @param occurrence the occurrence index
     * @return the index of the nth occurrence of the specified element in
     *        this list, or -1 if this list does not contain the element
     * @since 1.0.0
     * @see #lastIndexOf(E)
     * @see #indexOf(E, int)
     * @see #lastIndexOf(E, int)
     * @see #contains(E)
     * @see #contains(E, int)
     */
    public default int indexOf(E element, int occurrence) {
        int occurrencesFromBack = occurrences(element) - occurrence;
        if (occurrencesFromBack < 0) {
            return -1;
        } else if (occurrence == 1) {
            return indexOf(element);
        } else {
            int index = lastIndexOf(element);
            while (occurrencesFromBack-- > 0)
                index = splice(0, index).lastIndexOf(element);
            return index;
        }
    }

    /**
     * Returns the indices of the occurrences of the specified element in
     * this list, or an empty list if this list does not contain the element.
     * @param element element to search for
     * @return the indices of the occurrences of the specified element in
     *        this list, or an empty list if this list does not contain the element 
     * @since 1.0.0
     * @see #lastIndexOf(E)
     * @see #indexOf(E, int)
     * @see #lastIndexOf(E, int)
     * @see #contains(E)
     * @see #contains(E, int)
     */
    public default int[] indicesOf(E element) {
        int[] indices = new int[occurrences(element)];
        int count = 0;
        int previousIndex = 0;
        int nextIndex = -1;
        IndexBased<E> subStruct;
        do {
            subStruct = splice(previousIndex, size());
            nextIndex = subStruct.indexOf(element);
            previousIndex += nextIndex + 1;
            indices[count++] = previousIndex - 1;
        } while (count < indices.length);
        return indices;
    }

    /**
     * Returns the index of the last occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     * @param element element to search for
     * @return the index of the last occurrence of the specified element in
     *        this list, or -1 if this list does not contain the element
     * @since 1.0.0
     * @see #lastIndexOf(E)
     * @see #indexOf(E, int)
     * @see #lastIndexOf(E, int)
     * @see #contains(E)
     * @see #contains(E, int)
     */
    public int lastIndexOf(E element);

    /**
     * Returns the number of occurrences of the specified element in this list.
     * @param element element to search for
     * @return the number of occurrences of the specified element in this list
     * @since 1.0.0
     * @see #lastIndexOf(E)
     * @see #indexOf(E, int)
     * @see #lastIndexOf(E, int)
     * @see #contains(E)
     * @see #contains(E, int)
     */
    public default int occurrences(E element) {
        int count = 0;
        int previousIndex = 0;
        int nextIndex = -1;
        IndexBased<E> subStruct;
        do {
            subStruct = splice(previousIndex, size());
            nextIndex = subStruct.indexOf(element);
            if (nextIndex != -1) {
                count++;
                previousIndex += nextIndex + 1;
            }
        } while (nextIndex != -1);
        return count;
    }

    /*******************/
    /* BULK OPERATIONS */
    /*******************/

    /**
     * Inserts all of the elements in the specified collection into this list,
     * starting at the specified position. Shifts the element currently at that
     * position (if any) and any subsequent elements to the right. The new 
     * elements will appear in this list in the order that they are returned 
     * by the specified collection's iterator.
     * @param index index at which to insert the first element from the specified collection
     * @param collection collection containing elements to be added to this list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0} or {@code index > size()})
     * @throws NullPointerException if the specified collection is {@code null}
     * @see #add(E)
     * @since 1.0.0
     */
    public boolean addAll(int index, IndexBased<E> collection);

    /**
     * Appends the specified elements to the end of this list.
     * @param collection elements to be appended to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     * @since 1.0.0
     */
    // public abstract boolean addAll(IndexBased<E> collection);

    /**
     * Appends all elements in this list that match the given predicate with the
     * given element to the end of this list.
     * @param filter the predicate to use to filter the elements to be added
     * @param collection collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     * @since 1.0.0
     */
    public default boolean addIf(Predicate<? super E> filter, IndexBased<E> collection) {
        Objects.requireNonNull(filter);
        boolean added = false;
        final Iterator<? extends E> each = collection.iterator();
        while (each.hasNext()) {
            E element = each.next();
            if (filter.test(element)) {
                add(element);
                added = true;
            }
        }
        return added;
    }

    /**
     * Replaces the given elements in this list with the specified element.
     * @param collection  collection containing elements to be stored in this list
     * @param replacement element to be stored in this list
     * @return {@code true} if any of the elements were replaced
     */
    public default boolean replaceAll(Structure<E> collection, E replacement) {
        for (E element : collection) {
            if (contains(element)) {
                set(indexOf(element), replacement);
            }
        }
        return true;
    }

    /**
     * Replaces all elements in this list that match the given predicate with the
     * given element
     * @param predicate   a predicate matching elements to be replaced
     * @param replacement the element to replace matching elements with
     * @return {@code true} if any elements were replaced
     * @since 1.0.0
     */
    public default boolean replaceIf(Predicate<? super E> filter, E replacement) {
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

    /**
     * Determines if this list is empty.
     * @return {@code true} if this list is empty
     * @since 1.0.0
     */
    @Override
    public default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Splits this list into a sublist between the specified indices.
     * @param fromIndex the index of the first element to include in the sublist
     * @param toIndex the index of the last element to include in the sublist
     * @return the sublist between the specified indices
     * @throws IndexOutOfBoundsException if the specified indices are out of range
     */
    @SuppressWarnings("unchecked")
    public default IndexBased<E> splice(int fromIndex, int toIndex) {
        IndexBased<E> splicedStruct;
        try {
            splicedStruct = (IndexBased<E>) getClass().getDeclaredConstructor().newInstance();
            for (; fromIndex < toIndex; fromIndex++) {
                splicedStruct.add(get(fromIndex));
            }
            return splicedStruct;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Swaps the elements at the specified indices.
     * @param index the index of the first element
     * @param swapIndex the index of the second element
     */
    public default boolean swap(int index, int swapIndex) {
        E temp = get(index);
        set(index, get(swapIndex));
        set(swapIndex, temp);
        return true;
    }
}
