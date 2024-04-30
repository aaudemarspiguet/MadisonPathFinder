// == CS400 Spring 2024 File Header Information ==
// Name: Wyatt Federman
// Email: wfederman@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This Class implements a Hashtable that is used to store data from the
 * second CS400 project.
 * @param <KeyType> the type of key stored in the hashtable
 * @param <ValueType> the type of values stored in the hashtable
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    // Table that the key/value pairs are stored in
    protected LinkedList<Pair>[] table = null;

    // Class used for the key/value pairs
    protected class Pair {
        public KeyType key;
        public ValueType value;

        // Constructor for the key/value pairs
        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Constructor that creates a HashtableMap with a specified capacity
     * @param capacity the capacity of the HashtableMap
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        table = new LinkedList[capacity];
    }


    /**
     * Constructor that creates a Hashtable Map with the default capacity of 64
     */
    @SuppressWarnings("unchecked")
    public HashtableMap() {
        // with default capacity = 64
        table = new LinkedList[64];
    }

    /**
     * Adds a new key,value pair/mapping to the table
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        // Throws NullPointerException if key is null
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        // Calculates the key's index for later use
        int index = Math.abs(key.hashCode()) % getCapacity();
        // If index is empty, create a new linked list and add to index
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        } else {
            // Go through each key in the index and throw IllegalArgumentException if it already exists
            for (Pair pair : table[index]) {
                if(pair.key.equals(key)) {
                    throw new IllegalArgumentException("Key already exists");
                }
            }
        }
        // Add the new key/value pair and the appropriate index in the table
        table[index].add(new Pair(key, value));
        // If the load factor is greater than or equal to 80%, double the capacity of the table
        if((double) getSize() / getCapacity() >= 0.8) {
            // Create new LinkedList with double the previous capacity
            LinkedList<Pair>[] resizedTable = new LinkedList[getCapacity()*2];
            // Recalculate hashes for the new table
            for (LinkedList<Pair> chain : table) {
                if (chain != null) {
                    for (Pair pair : chain) {
                        int newIndex = Math.abs(pair.key.hashCode()) % (getCapacity() * 2);
                        if(resizedTable[newIndex] == null) {
                            resizedTable[newIndex] = new LinkedList<>();
                        }
                        resizedTable[newIndex].add(pair);
                    }
                }
            }
            // Update the old table to the new resized table
            table = resizedTable;
        }

    }

    /**
     * Checks whether a key maps to a value in this table.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        // If the key's hash doesn't map to a chain (LinkedList), return false
        if (table[Math.abs(key.hashCode()) % getCapacity()] == null) {
            return false;
        }
        // Go through each pair in the chain, if the key is found, return true
        for (Pair pair : table[Math.abs(key.hashCode()) % getCapacity()]) {
            if (pair.key.equals(key)) {
                return true;
            }
        }
        // Otherwise return false
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         table
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        // If the key's hash doesn't map to a chain (LinkedList), throw a NoSuchElementException
        if (table[Math.abs(key.hashCode()) % getCapacity()] == null) {
            throw new NoSuchElementException("Key not found");
        }
        // Go through each pair in the chain to see if there is a matching key
        for (Pair pair : table[Math.abs(key.hashCode()) % getCapacity()]) {
            if (pair.key.equals(key)) {
                return pair.value;
            }
        }
        // Throw NoSuchElementException if the key is not stored in this table
        throw new NoSuchElementException("Key not found");
    }

    /**
     * Remove the mapping for a key from the table.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this table
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        // If the key's hash doesn't map to a chain (LinkedList), throw a NoSuchElementException
        if (table[Math.abs(key.hashCode()) % getCapacity()] == null) {
            throw new NoSuchElementException("Key not found");
        }
        // Go through each pair in the chain to see if there is a matching key
        for (int i = 0; i < table[Math.abs(key.hashCode()) % getCapacity()].size(); i++) {
            Pair pair = table[Math.abs(key.hashCode()) % getCapacity()].get(i);
            // If a matching key is found, remove the key/value pair from the chain
            if (pair.key.equals(key)) {
                table[Math.abs(key.hashCode()) % getCapacity()].remove(i);
                return pair.value;
            }
        }
        // Throw NoSuchElementException if the key is not stored in this table
        throw new NoSuchElementException("Key not found");
    }

    /**
     * Removes all key,value pairs from the map.
     */
    @Override
    public void clear() {
        // Loop through the array and set each value to null
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    /**
     * Retrieves the number of keys stored in this map.
     * @return the number of keys stored in this map.
     */
    @Override
    public int getSize() {
        // Initialize size as 0
        int size = 0;
        // Loop through the array and add 1 to the size when the value isn't null
        for (LinkedList<Pair> chain : table) {
            if (chain != null) {
                size += chain.size();
            }
        }
        return size;
    }

    /**
     * Retrieves the map's capacity.
     * @return the size of the underlying array for the map
     */
    @Override
    public int getCapacity() {
        return table.length;
    }

    /**
     * Removes an element from the map and checks to see if a NoElementException
     * is thrown when checking to see if they key exists
     */
    @Test
    public void testRemove() {
        // Initialize new HashtableMap
        HashtableMap<Integer, String> map = new HashtableMap<>();

        // Insert three key/value pairs
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        // Remove one key/value pair
        map.remove(2);

        // Check if removing an invalid key/value pair throws an exception
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> map.remove(4)
        );

        // Check if the key/value pair was correctly removed
        Assertions.assertFalse(map.containsKey(2));
        Assertions.assertEquals(map.getSize(), 2);
    }

    /**
     * Adds elements to the map and checks to see if getSize()
     * returns the correct size.
     */
    @Test
    public void testPutCases() {
        // Initialize new HashtableMap
        HashtableMap<Integer, String> map = new HashtableMap<>();

        // Insert three key/value pairs
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        // Check to make sure key/value pairs exist
        Assertions.assertTrue(map.containsKey(1));
        Assertions.assertTrue(map.containsKey(2));
        Assertions.assertTrue(map.containsKey(3));

        // Check if put() throws an IllegalArgumentException when called with a duplicate key
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> map.put(3, "three")
        );

        // Check if put() throws an NullPointerException when called with a null key
        Assertions.assertThrows(
                NullPointerException.class,
                () -> map.put(null, "three")
        );

        // Check if only three key/value pairs were added to the map
        Assertions.assertEquals(map.getSize(), 3);
    }


    /**
     * Checks to see if the clear() function removes all key/value
     * pairs from the map. Also checks to see if key/value pairs can be added
     * after removal.
     */
    @Test
    public void testClear() {
        // Initialize new HashtableMap
        HashtableMap<Integer, String> map = new HashtableMap<>();

        // Insert four key/value pairs
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");

        // Check if the size of the map is 4
        Assertions.assertEquals(map.getSize(), 4);

        // Call the clear() method
        map.clear();

        // Check if the size of the map is 0
        Assertions.assertEquals(map.getSize(), 0);

        // Add a key/value pair again
        map.put(1, "one");

        // Check if key/value pairs can be added again after the map was cleared
        Assertions.assertTrue(map.containsKey(1));
    }

    /**
     * Tests the get method on a medium HashtableMap with keys 1, 2, 3, 4,
     * 5, and 6.
     */
    @Test
    public void testGetOnMediumMap() {
        // Initialize new HashtableMap
        HashtableMap<Integer, String> map = new HashtableMap<>();

        // Insert eight key/value pairs
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        map.put(6, "six");
        map.put(20, "twenty");
        map.put(432, "four hundred thirty two");

        // Check if get() returns the correct value with three different keys
        Assertions.assertEquals("five", map.get(5));
        Assertions.assertEquals("twenty", map.get(20));
        Assertions.assertEquals("four hundred thirty two", map.get(432));
    }


    /**
     * Checks to see if getCapacity() returns the corect capacity.
     */
    @Test
    public void testTableResizeTwice() {
        // Initialize new HashtableMap
        HashtableMap<Integer, String> map = new HashtableMap<>(4);

        // Insert three key/value pairs
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        // Check that the capacity of the map is still 4
        Assertions.assertEquals(4, map.getCapacity());

        // Add a fourth key/value pair
        map.put(4, "four");

        // Check if the capacity of the map doubled to 8
        Assertions.assertEquals(8, map.getCapacity());

        // Add two more key/value pairs
        map.put(5, "five");
        map.put(6, "six");

        // Check that the capacity of the map is still 8
        Assertions.assertEquals(8, map.getCapacity());

        // Add a seventh key/value pair
        map.put(7, "seven");

        // Check if the capacity of the map doubled to 16
        Assertions.assertEquals(16, map.getCapacity());

    }
}

