package main;

import java.util.*;
import main.util.Cloneable;

@SuppressWarnings("rawtypes")
public interface Structure<E> extends Iterable<E>, Comparator<Structure>, Cloneable<Structure> {
    public Object[] arrayify();
    //     if (this instanceof LinearStructure) {
    //         return ((LinearStructure<E>) (this)).toArray();
    //     }  else if (this instanceof AssociativeStructure) {
    //         return ((AssociativeStructure<E>) (this)).values().toArray();
    //     } else if (this instanceof TreeStructure) {
    //         return ((TreeStructure<E>) (this)).toList().toArray();
    //     } else if (this instanceof GraphStructure) {
    //         return ((GraphStructure<E>) (this)).toMap().values().toArray();
    //     }
    //     return null;
    // }

    public void clear();

    public Structure<E> clone();

    public int compare(Structure o1, Structure o2);

    public boolean equals(Object o);
    
    public int hashCode();
    
    public boolean isEmpty();
    
    public Iterator<E> iterator();

    public Iterator<E> reverseIterator();
   
    public int size();
  
    public String toString();
}