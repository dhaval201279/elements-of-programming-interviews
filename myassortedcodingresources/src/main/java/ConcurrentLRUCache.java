import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ConcurrentHashMap<K, V>: Used to store the key-value pairs. It provides thread-safe operations and high performance
 * for concurrent access.
 *
 * ConcurrentLinkedDeque<K>: Used to maintain the order of keys based on their usage. The most recently used key is at
 * the front, and the least recently used key is at the end.
 *
 * ReentrantReadWriteLock: Used to ensure thread safety during read and write operations. It allows multiple threads to
 * read simultaneously but ensures exclusive access for writes
 *
 * @param <K>
 * @param <V>
 */
public class ConcurrentLRUCache<K, V> {
    private final int capacity;
    private final ConcurrentHashMap<K, V> cache;
    private final ConcurrentLinkedDeque<K> deque;
    private final ReentrantReadWriteLock lock;

    public ConcurrentLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.deque = new ConcurrentLinkedDeque<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            if (cache.containsKey(key)) {
                // Move the accessed key to the front of the deque (most recently used)
                deque.remove(key); // Remove the key from its current position
                deque.addFirst(key); // Add it to the front
                return cache.get(key);
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            if (cache.size() >= capacity && !cache.containsKey(key)) {
                // Evict the least recently used key
                K evictedKey = deque.removeLast(); // Remove the last key (LRU)
                cache.remove(evictedKey); // Remove the evicted key from the cache
            }
            cache.put(key, value); // Add/update the key-value pair
            deque.remove(key); // Remove the key if it already exists in the deque
            deque.addFirst(key); // Add it to the front (most recently used)
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        return cache.size();
    }

    @Override
    public String toString() {
        return "Cache: " + cache + ", Deque: " + deque;
    }

    // Print cache contents (for debugging)
    public void printCache() {
        deque.forEach(key -> System.out.print(key + " -> "));
    }

    public static void main(String[] args) {
        ConcurrentLRUCache<Integer, String> cache = new ConcurrentLRUCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        cache.printCache();
        System.out.println("-- 1: " + cache.get(1)); // Output: One (moves 1 to the front)
        cache.printCache();
        cache.put(4, "D"); // Evicts 2 (LRU)
        System.out.println("-- 2: " + cache.get(2)); // Output: null (2 was evicted)
        cache.printCache();
        System.out.println("-- 4: " + cache.get(4)); // Output: null (2 was evicted)
        cache.printCache();
    }
}