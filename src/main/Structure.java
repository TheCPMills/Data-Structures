package main;

import java.util.*;

@SuppressWarnings("rawtypes")
public interface Structure extends Iterable, Comparator<Structure>, Cloneable {
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

    public Object clone();

    public int compare(Structure o1, Structure o2);

    public boolean equals(Object o);
    
    public int hashCode();
    
    public boolean isEmpty();
    
    public Iterator iterator();

    public Iterator reverseIterator();
   
    public int size();
  
    public String toString();
}