/**
 *
 * Shorthand for a class with two private fields with the appropriate
 * constructor and getters
 *
 */

public record HEntry<K, V>(K key, V value) {}

