package shortener.strategy;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class to represent a map entry (key-value pair) of custom created HashMaps (i. e. FileStorageStrategy and OurHashMapStorageStrategy)
 * HashMap.Node<K,V> class custom analogy
 */

public class Entry implements Serializable {
    Long key;
    String value;
    Entry next;
    int hash;

    public Entry(int hash, Long key, String value, Entry next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public Long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return Objects.equals(key, entry.key) &&
                Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }

    public String toString() {
        return key + "=" + value;
    }
}
