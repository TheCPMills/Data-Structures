package main.associative;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import main.*;
import main.linear.*;

public class HashMap<K, V> extends AssociativeStructure<K, V> {
    /* the number of entries in the hash table */
    transient int size;
    /* the number of modifications to the hash table */
    transient int modCount;
    /* the maxinum number of entries in the hash table before it must be resized */
    int threshold;
    /* the load factor of the hash table */
    public final float loadFactor;
    /* the table, an array of linked lists */
    LinkedList<Entry<K, V>>[] table;

    /*
     * Constructs a new, empty hash map with the default initial capacity and
     * load factor.
     */
    public HashMap() {
        this(DEFAULT_LOAD_FACTOR);
    }

    /*
     * Constructs a new, empty hash map with the default initial capacity and the
     * specified load factor.
     */
    public HashMap(float loadFactor) {
        this.loadFactor = loadFactor;
        init(DEFAULT_INITIAL_CAPACITY);
    }

    /*
     * Constructs a new hash map with the same mappings as the specified map.
     */
    public HashMap(AssociativeStructure<K, V> c) {
        this(c.loadFactor());
        putAll(c);
    }
    
    
    @Override
    /*
     * Removes all the entries from this map.
     */
    public void clear() {
        for (K key : keys()) {
            remove(key);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int compare(Structure o1, Structure o2) {
        if (o1.getClass() != o2.getClass()) {
            throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
        } else {
            int o1Size = o1.size();
            int o2Size = o2.size();

            Class<?> o1KeyParamClass = (o1Size > 0) ? ((HashMap<K, V>) (o1)).keys().get(0).getClass() : null;
            Class<?> o2KeyParamClass = (o2Size > 0) ? ((HashMap<K, V>) (o2)).keys().get(0).getClass() : null;
            Class<?> o1ValueParamClass = (o1Size > 0) ? ((HashMap<K, V>) (o1)).values().get(0).getClass() : null;
            Class<?> o2ValueParamClass = (o2Size > 0) ? ((HashMap<K, V>) (o2)).values().get(0).getClass() : null;

            if (o1Size != 0 && o2Size != 0) {
                if (o1KeyParamClass != o2KeyParamClass) {
                    throw new IllegalArgumentException(o1KeyParamClass + " cannot be compared to " + o2KeyParamClass);
                }
                if (o1ValueParamClass != o2ValueParamClass) {
                    throw new IllegalArgumentException(o1ValueParamClass + " cannot be compared to " + o2ValueParamClass);
                }
            } else if (o1Size == 0 && o2Size == 0) {
                return 0;
            }
        
            int result;
            Comparable<Entry<K, V>> obj1;
            Iterator<Entry<K,V>> i1 = o1.iterator();
            Iterator<Entry<K, V>> i2 = o2.iterator();
            int smallerSize = Math.min(o1Size, o2Size);
            for (int i = 0; i < smallerSize; i++) {
                obj1 = (Comparable<Entry<K, V>>) i1.next();
                Entry<K, V> obj2 = i2.next();
                result = obj1.compareTo(obj2);
                if (result != 0) {
                    return result;
                }
            }
            return Integer.compare(o1Size, o2Size);
        }
    }

    @Override
    /*
     * Returns true if this map contains no entries.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
    }

    @Override
    public Iterator<Entry<K,V>> reverseIterator() {
        return // new EntryIterator(true);
        null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public HashMap<K,V> clone() {
        return new HashMap<K, V>(this);
    }

    @Override
    public LinkedList<Entry<K, V>> entries() {
        LinkedList<Entry<K, V>> entries = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                entries.addAll(table[i]);
            }
        }
        return entries;
    }

    @Override
    public Set<K> keys() {
        Set<K> keys = new Set<>();
        for (Entry<K, V> entry : entries()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public List<V> values() {
        List<V> values = new List<>();
        for (Entry<K, V> entry : entries()) {
            values.add(entry.getValue());
        }
        return values;
    }

    @Override
    @SuppressWarnings("unused")
    /*
     * Maps the specified value to the specified key.
     * @throws IllegalArgumentException if the key is already in the map.
     */
    public boolean put(K key, V value) {
        LinkedList<Entry<K, V>>[] tab;
        LinkedList<Entry<K, V>> p;
        int n, i;

        // get the hash code of the key
        int hash = hash(key);

        // if the table already contains the key, throw an exception
        if (containsKey(key)) {
            throw new IllegalArgumentException("Key already in map");
        }

        // if the table is not big enough, resize the table.
        if (size - 1 >= threshold) {
            resize();
        }

        n = (tab = table).length;

        // if the index in the table is null, add a new list there
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = new LinkedList<Entry<K, V>>();
        }

        // add the entry to the list
        tab[i].add(new Entry<>(key, value));

        modCount++;
        size++;
        return true;
    }

    @Override

    public boolean putAll(AssociativeStructure<K, V> c) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * Returns the value to which the specified key is mapped, or null if this map
     * contains no mapping for the key.
     */
    public V get(K key) {
        // get the hash code of the key
        int hash = hash(key);
        // get the index of the table
        int i = (table.length - 1) & hash;
        // get the list at the index
        LinkedList<Entry<K, V>> list = table[i];
        // if the list is not null, search for the key
        if (list != null) {
            for (Entry<K, V> e : list) {
                if (e.getKey().equals(key)) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        // get list of keys
        Set<K> keys = keys();
        // search for the key
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        // get list of values
        List<V> values = values();
        // search for the value
        return values.contains(value);
    }

    @Override
    public V replace(K key, V value) {
        // get the hash code of the key
        int hash = hash(key);
        // get the index of the table
        int i = (table.length - 1) & hash;
        // get the list at the index
        LinkedList<Entry<K, V>> list = table[i];
        // if the list is not null, search for the key
        if (list != null) {
            for (Entry<K, V> e : list) {
                if (e.getKey().equals(key)) {
                    V oldValue = e.getValue();
                    e.setValue(value);
                    return oldValue;
                }
            }
        }
        // if the key is not found, put the key and value in the table
        put(key, value);
        return null;
    }

    @Override
    public V remove(K key) {
        // get the hash code of the key
        int hash = hash(key);
        // get the index of the table
        int i = (table.length - 1) & hash;
        // get the list at the index
        LinkedList<Entry<K, V>> list = table[i];
        // if the list is not null, search for the key
        if (list != null) {
            for (Entry<K, V> e : list) {
                if (e.getKey().equals(key)) {
                    V oldValue = e.getValue();
                    list.remove(e);
                    size--;
                    modCount++;
                    return oldValue;
                }
            }
        }
        return null;
    }

    @Override
    /**
     * Determines the keys that are associated with the given value
     * @param value the value to search for
     * @return a set of keys that are associated with the given value
     * @throws IllegalArgumentException if the value is not found
     * @throws NullPointerException if the value is null
     */
    public Set<K> keysOf(V value) {
        Set<K> keys = new Set<>();
        for (Entry<K, V> entry : entries()) {
            if (entry.getValue().equals(value)) {
                keys.add(entry.getKey());
            }
        }
        // if the value is not found, throw an exception
        if (keys.size() == 0) {
            throw new IllegalArgumentException("Value not found");
        }
        return keys;
    }

    public String toString() {
        return Arrays.toStringNonNull(table);
    }

    @Override
    public float loadFactor() {
        return loadFactor;
    }

    // helper methods
    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void init(int capacity) {
        int n = -1 >>> Integer.numberOfLeadingZeros(capacity - 1);
        int tableSize = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

        table = new LinkedList[tableSize];
        threshold = (int) (capacity * loadFactor);
    }

    @SuppressWarnings("unchecked")
    private final void resize() {
        LinkedList<Entry<K, V>>[] oldTab = table.clone();
        int capacity = oldTab.length;
        if(capacity != MAXIMUM_CAPACITY) {
            int newCapacity = (capacity * 2 >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : capacity * 2;
            table = new LinkedList[newCapacity];

            for (int i = 0; i < capacity; i++) {
                table[i] = oldTab[i];
            }

            threshold = (int) (newCapacity * loadFactor);
        }
    }

    private abstract class HashIterator {
        Entry<K, V> next; // next entry to return
        Entry<K, V> current; // current entry
        int expectedModCount; // for fast-fail
        int index; // current slot
        int listIndex; // current index in list

        HashIterator() {
            expectedModCount = modCount;
            LinkedList<Entry<K, V>>[] t = table;
            current = next = null;
            index = 0;
            listIndex = 0;
            if (t != null && size > 0) { // advance to first entry
                while (index < t.length && (t[index]) == null) {
                    index++;
                }
                next = t[index].get(listIndex++);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public final Entry<K, V> nextEntry() {
            LinkedList<Entry<K, V>>[] t;
            Entry<K, V> e = next;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (e == null) {
                throw new NoSuchElementException();
            }
            if((t = table) != null) {
                while (t[index] == null) {
                    index++;
                }
            }
            if(listIndex >= t[index].size()) {
                listIndex = 0;
                do {
                    index++;
                } while (index < t.length && t[index] == null);
                next = (index >= t.length) ? null : t[index].get(listIndex++);
            } else {
                next = t[index].get(listIndex++);;
            }
            current = e;
            return e;
        }

        public final void remove() {
            Entry<K, V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            current = null;
            HashMap.this.remove(p.getKey());
            expectedModCount = modCount;
        }
    }

    private final class KeyIterator extends HashIterator implements Iterator<K> {
        public final K next() {
            return nextEntry().getKey();
        }
    }

    private final class ValueIterator extends HashIterator implements Iterator<V> {
        public final V next() {
            return nextEntry().getValue();
        }
    }

    private final class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {
        public final Entry<K, V> next() {
            return nextEntry();
        }
    }

}