package durdinstudios.wowarena.misc

/**
 * Utility methods to work with immutable collections.
 */

/**
 * Replace or append the first item that matches the predicate.
 */
fun <T> List<T>.replace(replacement: T, predicate: (T) -> Boolean): List<T> {
    val idx = this.indexOfFirst(predicate)
    return if (idx == -1) {
        this + replacement
    } else {
        val mutable = this.toMutableList()
        mutable[idx] = replacement
        mutable
    }
}

/**
 * Replace the item in the given position.
 */
fun <T> List<T>.replaceAt(replacement: T, position: Int): List<T> {
    val mutable = this.toMutableList()
    mutable[position] = replacement
    return mutable
}

/**
 * Remove all items that match the filter
 */
fun <T> List<T>.remove(filter: (T) -> Boolean): List<T> {
    val newList = this.toMutableList()
    newList.removeAll { filter(it) }
    return newList
}

/**
 * Remove the item in the given position.
 */
fun <T> List<T>.removeAt(position: Int): List<T> {
    val mutable = this.toMutableList()
    mutable.removeAt(position)
    return mutable
}

/**
 * Replace the item with the given key.
 */
fun <K, V> Map<K, V>.replace(key: K, value: V): Map<K, V> {
    val newList = this.toMutableMap()
    newList[key] = value
    return newList
}

/**
 * Add the pair to the map keeping iteration order with the new element being the head
 */
infix fun <K, V> Map<K, V>.putFirst(pair: Pair<K, V>): Map<K, V> = putFirst(pair.first, pair.second)

/**
 * Add the item to the map keeping iteration order with the new element being the head
 */
fun <K, V> Map<K, V>.putFirst(key: K, value: V): Map<K, V> =
//Remove the value from the current map, add it back as the first element
    mapOf(key to value) + this.minus(key)