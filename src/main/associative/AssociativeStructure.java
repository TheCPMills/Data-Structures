package main.associative;

import main.*;
import main.linear.Set;
import main.linear.List;
import main.linear.LinkedList;

public abstract class AssociativeStructure<K, V> implements Structure<Entry<K, V>> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    public Object[] arrayify() {
        return this.values().toArray();
    }
    
    public abstract AssociativeStructure<K, V> clone();
    
    public abstract LinkedList<Entry<K, V>> entries();

    public abstract Set<K> keys();

    public abstract List<V> values();

    public abstract boolean put(K key, V value);

    public abstract boolean putAll(AssociativeStructure<K, V> c);

    public abstract V get(K key);

    public abstract boolean containsKey(K key);

    public abstract boolean containsValue(V value);

    public abstract V replace(K key, V value);

    public abstract V remove(K key);

    public abstract Set<K> keysOf(V value);

    public abstract float loadFactor();
}