package main.associative;

import java.util.Iterator;
import main.Structure;
import main.linear.LinkedList;
import main.linear.Set;
import main.linear.List;

public class MultiHashMap<K, V> extends AssociativeStructure<K, V> {

    public MultiHashMap() {
    }

    public MultiHashMap(float loadFactor) {
    }

    public MultiHashMap(AssociativeStructure<K, V> c) {
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    @SuppressWarnings("rawtypes")
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
    public Iterator<Entry<K,V>> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Entry<K, V>> reverseIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MultiHashMap<K, V> clone() {
        return new MultiHashMap<K, V>(this);
    }

    @Override
    public LinkedList<Entry<K, V>> entries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<K> keys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<V> values() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean put(K key, V value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean putAll(AssociativeStructure<K, V> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public V get(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        // TODO Auto-generated method stub
        return false;
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
    public Set<K> keysOf(V value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float loadFactor() {
        // TODO Auto-generated method stub
        return 0;
    }

    // helper functions

}
