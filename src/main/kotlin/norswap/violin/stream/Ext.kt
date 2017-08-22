package norswap.violin.stream

import norswap.violin.utils.after
//import java.util.stream.StreamSupport
//import java.util.stream.Stream as JStream

/**
 * Contents
 * [1] Create Streams
 * [2] Conversion to Stream
 * [3] Conversion from Stream
 * [4] Conversion to Streamable
 * [5] Conversion from Streamable
 * [6] Stream from Collections
 */

///

/**
 * Returns the toStream consisting of the receiver and the transitive closure of function [f] over
 * this receiver. Each item in the sequence (excepted the first) is the result of applying [f] on
 * the previous item.
 */
fun <T : Any> T.transitive(f: (T) -> T?): Stream<T> {
    var first = true
    var last: T? = this
    return Stream {
        if (first) last after { first = false }
        else if (last == null) null
        else f(last as T) after { last = it }
    }
}

/// [2] Conversion to Stream ///////////////////////////////////////////////////////////////////////

/**
 * Converts an iterator into a toStream.
 */
fun <T : Any> Iterator<T>.toStream(): Stream<T>
        = Stream { if (hasNext()) next() else null }



/**
 * Converts an iterator into a toStream, or an empty toStream if null.
 * (f = force)
 */
fun <T : Any> Iterator<T>?.fstream(): Stream<T>
        = this?.toStream() ?: Stream()



/**
 * Converts a java toStream into a violin toStream.
 */
fun <T : Any> java.util.stream.Stream<T>.toStream(): Stream<T> {
    val iterator = iterator()
    return Stream { if (iterator.hasNext()) iterator.next() else null }
}



/**
 * Converts a java toStream into a violin toStream, or an empty toStream if null.
 * (f = force)
 */
fun <T : Any> java.util.stream.Stream<T>?.fstream() = this?.toStream() ?: Stream()



/**
 * Returns a toStream consisting of the items of the iterable.
 */
fun <T : Any> Iterable<T>.toStream()
        = iterator().toStream()


/**
 * Returns a toStream consisting of the items of the iterable, or an empty toStream if null.
 * (f = force)
 */
fun <T : Any> Iterable<T>?.fstream()
        = this?.toStream() ?: Stream()



/**
 * Returns a toStream consisting of the items of the sequence.
 */
fun <T : Any> List<T>.toStream() = iterator().toStream()



/**
 * Returns a toStream consisting of the items of the sequence.
 */
fun <T : Any> Sequence<T>.toStream(): Stream<T>
        = iterator().toStream()



/**
 * Returns a toStream consisting of the items of the sequence, or an empty toStream if null.
 * (f = force)
 */
fun <T : Any> Sequence<T>?.fstream(): Stream<T>
        = this?.toStream() ?: Stream()

/// [3] Conversion from Stream /////////////////////////////////////////////////////////////////////

/**
 * Converts this to a kotlin sequence.
 *
 * The toStream and the sequence are linked:
 * items consumed within one will not show up in the other.
 */
fun <T : Any> Stream<T>.toSequence()
        = Sequence { iterator() }



/*
*
 * Converts this to a java toStream.
 *
 * Both streams are linked:
 * items consumed within one will not show up in the other.
fun <T : Any> Stream<T>.toJavaStream(): AutoCloseable = StreamSupport.stream(
        java.lang.Iterable<@kotlin.UnsafeVariance T> { iterator() }.spliterator(),
        false
)
*/

/// [4] Conversion to Streamable ///////////////////////////////////////////////////////////////////

/**
 * Converts an iterable into a streamable.
 */
fun <T : Any> Iterable<T>.streamable() = object : Streamable<T> {
    /**  */
    override fun stream() = this@streamable.iterator().toStream()
}



/**
 * Converts a kotlin sequence into a streamable.
 */
fun <T : Any> Sequence<T>.streamable() = object : Streamable<T> {
    /**  */
    override fun stream() = this@streamable.iterator().toStream()
}

/// [5] Conversion from Streamable ///////////////////////////////////////////////////////////////////

/**
 * Returns an iterable backed by the streamable.
 */
fun <T : Any> Streamable<T>.iterable() = object : Iterable<T> {
    /**  */
    override fun iterator() = this@iterable.stream().iterator()
}

/**
 * Returns a sequence backed by the streamable.
 */
fun <T : Any> Streamable<T>.sequence() = object : Sequence<T> {
    /**  */
    override fun iterator() = this@sequence.stream().iterator()
}

/// [6] Stream from Collections ////////////////////////////////////////////////////////////////////

/**
 * Returns a toStream consisting of the items of the array.
 */
fun <T : Any> Array<out T>.toStream(): Stream<T> {
    var i = 0
    return Stream { if (i < size) get(i++) else null }
}



/**
 * Returns a toStream consisting of the items of the array, in reverse order.
 */
fun <T : Any> Array<out T>.reverseStream(): Stream<T> {
    var i = size
    return Stream { if (i > 0) get(--i) else null }
}



/**
 * Returns a toStream consisting of the non-null items of the array.
 */
fun <T : Any> Array<out T?>.pureStream(): Stream<T> {
    var i = 0
    return Stream {
        var item: T? = null
        while (i < size && item == null) item = get(i++)
        item
    }
}



/**
 * Returns a toStream consisting of the non-null items of the array, in reverse order.
 */
fun <T : Any> Array<out T?>.pureReverseStream(): Stream<T> {
    var i = size
    return Stream {
        var item: T? = null
        while (i > 0 && item == null) item = get(--i)
        item
    }
}


/**
 * Returns a toStream consisting of the items of the list, in reverse order.
 */
fun <T : Any> List<T>.reverseStream(): Stream<T> = reversed(). fstream()


