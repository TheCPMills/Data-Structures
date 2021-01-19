package main;

import java.util.*;
import java.util.function.*;

public interface BidirectionalIterator<E> extends Iterator<E>{
    public void forEachRemaining(Consumer<? super E> action);
    
    public boolean hasNext();

    public boolean hasPrevious();

    public E next();

    public int nextIndex();

    public E previous();

    public int previousIndex();

    public void remove();
}