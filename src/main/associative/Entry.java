package main.associative;

import java.util.Comparator;
import java.util.Objects;

import main.util.*;
import main.util.Cloneable;

public class Entry<K, V> implements Comparable<Entry<K, V>> {
    private final K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int compareTo(Entry<K, V> o) {
        Class<?> keyParamClass = this.key.getClass();
        Class<?> valueParamClass = this.value.getClass();

        int keyComparison;
        if (Utilities.isRelatedTo(keyParamClass, Comparator.class)) {
            Comparator<Object> obj;
            try {
                obj = (Comparator<Object>) keyParamClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                if (Utilities.isRelatedTo(keyParamClass, Cloneable.class)) {
                    obj = (Comparator<Object>) ((Cloneable) (this.key)).clone();
                } else {
                    obj = (Comparator<Object>) this.key;
                }
            }
            keyComparison = obj.compare(this.key, o.key);
        } else if (Utilities.isRelatedTo(this.key.getClass(), Comparable.class)) {
            keyComparison = ((Comparable<K>) (this.key)).compareTo(o.key);
        } else {
            String s = keyParamClass.getName();
            throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
        }

        if (keyComparison == 0) {
            int valueComparison;
            if (Utilities.isRelatedTo(valueParamClass, Comparator.class)) {
                Comparator<Object> obj;
                try {
                    obj = (Comparator<Object>) valueParamClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    if (Utilities.isRelatedTo(valueParamClass, Cloneable.class)) {
                        obj = (Comparator<Object>) ((Cloneable) (this.value)).clone();
                    } else {
                        obj = (Comparator<Object>) this.value;
                    }
                }
                valueComparison = obj.compare(this.value, o.value);
            } else if (Utilities.isRelatedTo(this.value.getClass(), Comparable.class)) {
                valueComparison = ((Comparable<V>) (this.value)).compareTo(o.value);
            } else {
                String s = valueParamClass.getName();
                throw new IllegalArgumentException(s.substring(s.indexOf("class ")) + "'s cannot be compared");
            }
            return valueComparison;
        } else {
            return keyComparison;
        }
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