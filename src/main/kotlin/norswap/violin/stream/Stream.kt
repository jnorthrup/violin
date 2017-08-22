package norswap.violin.stream

import norswap.violin.utils.after
import java.util.*

/**
 * A toStream is a (potentially infinite) sequence of items.
 * As such, a toStream usually describes a lazy computation.
 */
interface Stream<out T : Any> {


    companion object {
        /**
         * `Stream { ... }` is sugar for
         * `object: Stream<U> { override fun next() = ... }`.
         */
        inline operator fun <U : Any> invoke(crossinline nextImpl: () -> U?) = object : Stream<U> {
            /**  */
            override fun next() = nextImpl()
        }



        /**
         * `Stream(1, 2, 3)` is sugar for `arrayOf(1, 2, 3).toStream()`.
         */
        operator fun <U : Any> invoke(vararg items: U) = items.toStream()



        /**
         * An empty toStream, assignable to Stream<T> for any T.
         */
        val empty = Stream { null }
    }



    /**
     * Returns the next item in the toStream, or ´null´ if the toStream is the toStream is over.
     *
     * Once this returns `null`, subsequent calls must return ´null´ as well.
     */
    fun next(): T?



    /**
     * Converts the toStream to an iterator.
     *
     * The toStream and the iterator are linked:
     * items consumed within one will not show up in the other.
     *
     * This method ensures that streams can be used in Kotlin for-loops.
     * This is why it isn't called `toIterator`, and why it is not an extension method
     * (to make for loops available without the need to import extension methods).
     */
    operator fun iterator() = object : Iterator<T> {
        private var peek: T? = this@Stream.next()
        /**  */
        override fun hasNext() = peek != null
        /**  */
        override fun next(): T
                = (peek ?: throw NoSuchElementException()) after { peek = this@Stream.next() }
    }
}
