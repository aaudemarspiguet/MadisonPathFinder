import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashtableMap<KeyType,ValueType> implements MapADT<KeyType, ValueType> {

    protected class Pair {

        public KeyType key;
        public ValueType value;
    
        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    
    }

    protected LinkedList<Pair>[] table;
    protected int capacity;
    private int size;
    private static final double LIMIT = 0.8;

    public HashtableMap() { // with default capacity = 64 
        this(64);
    }

    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.capacity = capacity;
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        size = 0;
    }

    @Override
    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException();
        }

        if (containsKey(key)) {
            throw new IllegalArgumentException();
        }

        // update size and rehash if necessary
        size++;
        if (size / ((double)capacity) >= LIMIT) {
            rehash();
        }

        // get index of table where to store
        int index = hash(key);

        // instantiate new List if null;
        if (table[index] == null) {
            table[index] = new LinkedList<Pair>();
        }

        table[index].add(new Pair(key, value));
    }

    @Override
    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    public boolean containsKey(KeyType key) {
        
        // false if null key or table at index is null
        if (key == null || table[hash(key)] == null) {
            return false;
        }

        for (int i = 0; i < (table[hash(key)]).size(); i++) {
            if ((table[hash(key)]).get(i).key.equals(key)){
                return true;
            }
        }

        return false; // default return, if not found
    }

    @Override
    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType get(KeyType key) throws NoSuchElementException {

        if (containsKey(key)) { 
            
            // iterate through linked list at the hashed index of the key
            for (int i = 0; i < table[hash(key)].size(); i++) {
                if (table[hash(key)].get(i).key.equals(key)) { // if found
                    return table[hash(key)].get(i).value;
                }
            }
        }

        throw new NoSuchElementException(); // finally, default return if key doesn't exist in table
    }

    @Override
    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType remove(KeyType key) throws NoSuchElementException {

        if (containsKey(key)) {

            // iterate through linked list stored at index of key
            for (int i = 0; i < table[hash(key)].size(); i++) {
                if ((table[hash(key)].get(i).key).equals(key)) {
                    size--; // decrement size;
                    return table[hash(key)].remove(i).value;
                }
            }
        }

        throw new NoSuchElementException(); // default return if key doesn't exist in table
    }

    @SuppressWarnings("unchecked")
    @Override
     /**
     * Removes all key, value pairs from this collection.
     */
    public void clear() {

        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        size = 0; 
        
    }

    @Override
    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    public int getSize() {
        return size;
    }

    @Override
     /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * return index of where in the table to store the key
     * @return int of where to store key
     */
    private int hash(KeyType key) {
        return (Math.abs(key.hashCode())) % capacity;

    }  

     /**
     * rehash the table with a new table of double the capacity
     * @void
     */
    @SuppressWarnings("unchecked")
    private void rehash() {

        // instantiate new table with double capacity and update current capacity
        LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[2 * capacity];
        capacity *= 2;

        for (int i = 0; i < table.length; i++) {

            // copies over non-null key value pairs
            if (table[i] != null) {

                // iterate through linked list stored at the index, adding all keys to new table
                for (int j = 0; j < table[i].size(); j++) {
                    int newIndex = hash(table[i].get(j).key);
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<Pair>();
                    }
                    newTable[newIndex].add(table[i].get(j));
                }
            }
        }

        table = newTable;
    }

    // MIDWEEK

    HashtableMap<String, Integer> hashtable;

    @BeforeEach
    void setUp() {
        hashtable = new HashtableMap<String, Integer>(3); // capacity at 3
    }

    /**
     * Test to check if a key-value pair can be successfully added to the HashtableMap.
     */
    @Test
    void testPut() {
        hashtable.put("key1", 1);
        hashtable.put("key2", 2);

        // should rehash and double capacity at this point;
        hashtable.put("key3", 3);

        assertTrue(hashtable.containsKey("key1"));
        assertTrue(hashtable.containsKey("key2"));
        assertTrue(hashtable.containsKey("key3"));

        assertTrue(hashtable.getCapacity() == 6 && hashtable.getSize() == 3);


    }

    /**
     * Test to check if the containsKey method returns true when the key is present in the HashtableMap.
     */
    @Test
    void testContainsKey() {
        hashtable.put("key1", 1);
        hashtable.put("X01", 2);

        assertFalse(hashtable.containsKey("key5"));
        assertTrue(hashtable.containsKey("key1"));
        assertTrue(hashtable.containsKey("X01"));
        System.out.println("X01".hashCode());
        System.out.println(hashtable.hash("X01"));
    }

    /**
     * Test to check if the get method retrieves the correct value associated with a key in the HashtableMap.
     */
    @Test
    void testGet() {
        hashtable.put("key1", 1);

        try {
            hashtable.get("key5");
            assertFalse(false);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        assertEquals(1, hashtable.get("key1"));

        
    }

    /**
     * Test to check if removing a key-value pair from the HashtableMap decreases its size by 1.
     */
    @Test
    void testRemove() {
        hashtable.put("key1", 1);
        assertEquals(1, hashtable.remove("key1"));
        assertEquals(0, hashtable.getSize());
    }

    /**
     * Test to check if the clear method removes all key-value pairs from the HashtableMap.
     */
    @Test
    void testClear() {
        hashtable.put("key1", 1);
        hashtable.put("key2", 2);
        hashtable.clear();
        assertEquals(0, hashtable.getSize());
    }
}
