package util;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<K,V> extends Map<K, List<V>> {
    void add(K key, V value);
    void addAll(K key, List<? extends V> values);
    void addAll(MultiValueMap<K, V> values);
    V getFirst(K key);
    void set(K key, V value);
    void setAll(Map<K, V> values);
}
