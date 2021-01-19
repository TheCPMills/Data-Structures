package main.associative;

import java.util.Enumeration;
import java.util.Iterator;

import main.*;
import main.linear.*;

public class HashMap<K, V> extends AssociativeStructure<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    transient int size;
    transient int modCount;
    int threshold;
    final float loadFactor;
    LinkedList<Entry<K, V>>[] table;

    public HashMap() {
        this(DEFAULT_LOAD_FACTOR);
    }

    public HashMap(float loadFactor) {
        this.loadFactor = loadFactor;
        init(DEFAULT_INITIAL_CAPACITY);
    }
    
    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public int compare(Structure o1, Structure o2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator reverseIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<Entry<K, V>> entries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinearStructure<K> keys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinearStructure<V> values() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean put(K key, V value) {
        LinkedList<Entry<K, V>>[] tab;
        LinkedList<Entry<K, V>> p;
        int n, i;

        // get the hash code of the key
        int hash = hash(key);

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
    public boolean putAll() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public V get(K key) {
        // Entry<K, V> e = getNode(key)
        // return (e == null) ? null : e.getValue();
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        //return getNode(key) != null;
        return true;
    }

    @Override
    public boolean containsValue(V value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public V replace(K key, V value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V remove(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinearStructure<K> keysOf(V value) {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        return Arrays.toStringNonNull(table);
    }

    // helper methods
    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void checkForDuplicateKey(K key) {
    }

    private void init(int capacity) {
        int n = -1 >>> Integer.numberOfLeadingZeros(capacity - 1);
        int tableSize = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

        table = new LinkedList[tableSize];
        threshold = capacity - (int) (capacity * loadFactor);
    }

    private final void resize() {
        LinkedList<Entry<K, V>>[] oldTab = table.clone();
        int capacity = oldTab.length;
        if(capacity != MAXIMUM_CAPACITY) {
            int newCapacity = (capacity * 2 >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : capacity * 2;
            table = new LinkedList[newCapacity];

            for (int i = 0; i < capacity; i++) {
                table[i] = oldTab[i];
            }

            threshold = (int) (capacity * loadFactor);
        }
    }
}