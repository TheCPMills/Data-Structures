package main.associative;

import java.util.*;

import main.*;
import main.linear.*;

public abstract class AssociativeStructure<K, V> implements Structure {
    public Object[] arrayify() {
        return this.values().toArray();
    }
    
    public abstract Object clone();
    
    public abstract Enumeration<Entry<K, V>> entries();

    public abstract LinearStructure<K> keys();

    public abstract LinearStructure<V> values();

    public abstract boolean put(K key, V value);

    public abstract boolean putAll();

    public abstract V get(K key);

    public abstract boolean containsKey(K key);

    public abstract boolean containsValue(V value);

    public abstract V replace(K key, V value);

    public abstract V remove(K key);

    public abstract LinearStructure<K> keysOf(V value);
}

class Entry<K, V> {
    private final K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public void setValue(V newValue) {
        value = newValue;
    }

    public String toString() {
        return key + ": " + value;
    }
}