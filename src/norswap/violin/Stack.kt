package norswap.violin
import norswap.violin.stream.PeekStream
import norswap.violin.stream.Streamable

interface Stack<T: Any>: Streamable<T> {
    override fun stream(): PeekStream<T>
    val size: Int
    val empty: Boolean
        get() = size == 0
    fun push(item: T)
    fun peek(): T?
    fun pop(): T?

    /**
     * Pop items from the stack until its size is [target].
     * If `size > target`, does nothing.
     */
    fun truncate(target: Int) {
        while (size > target) pop()
    }

    /**
     * Returns a stream consisting of the items of the stack.
     * Consuming items from the stream (with [next]) pops them from the stack.
     */
    fun poppingStream() = object: PeekStream<T> {
        override fun peek() = this@Stack.peek()
        override fun next() = this@Stack.pop()
    }
}
