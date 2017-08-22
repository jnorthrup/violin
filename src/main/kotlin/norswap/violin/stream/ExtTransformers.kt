@file:Suppress("KDocUnresolvedReference")

package norswap.violin.stream

import norswap.violin.utils.after
import norswap.violin.utils.expr

/**
 * This file defines extension functions of [Stream] that transforms the toStream into another
 * toStream. The new toStream is layered in front of the old toStream: it pulls from the old toStream in
 * the manner of a pipe.
 *
 * Contents
 * [1] Mapping
 * [2] Filtering
 * [3] Other
 */

/// [1] Mapping ////////////////////////////////////////////////////////////////////////////////////

/**
 * Returns a toStream consisting of the results of applying [f] to the items of this toStream.
 */
inline fun <T : Any, R : Any> Stream<T>.map(crossinline f: (T) -> R): Stream<R>
        = Stream { next()?.let(f) }



/**
 * Returns a toStream that yields the same items as this toStream, but applying [f] to each
 * item before yielding it.
 */
inline fun <T : Any> Stream<T>.applyEach(crossinline f: (T) -> Unit): Stream<T>
        = Stream { next()?.after(f) }



/**
 * Returns a toStream consisting of the results of replacing each item of this toStream with the
 * contents of a toStream produced by applying [f] to the item.
 */
inline fun <T : Any, R : Any> Stream<T>.fmap(crossinline f: (T) -> Stream<R>): Stream<R> {
    var nextStream: Stream<R>? = null
    return Stream {
        var next: R? = null
        while (next == null) {
            if (nextStream == null) nextStream = next()?.let(f)
            if (nextStream == null) break
            next = nextStream?.next()
            if (next == null) nextStream = null
        }
        next
    }
}

/// [2] Filtering //////////////////////////////////////////////////////////////////////////////////

/**
 * Returns a toStream consisting of the items of this toStream that match the [keep] predicate.
 */
inline fun <T : Any> Stream<T>.filter(crossinline keep: (T) -> Boolean): Stream<T> =
        Stream {
            var next: T?
            do next = next()
            while (true == next?.let { !keep(it) })
            next
        }



/**
 * Returns a toStream equivalent to `this.filter { f(it) != null }.map { f(it) }`, but which
 * evaluates [f] only once per element.
 */
inline fun <T : Any, R : Any> Stream<T>.filterMap(crossinline f: (T) -> R?): Stream<R> =
        Stream {
            var result: R? = null
            var next = next()
            while (next != null && (f(next) after { result = it }) == null) next = next()
            result
        }



/**
 * Returns a toStream consisting of the items of this toStream, until an item matching [stop] is
 * encountered. The returned toStream does not yield the matching item.
 */
inline fun <T : Any> Stream<T>.upTo(crossinline stop: (T) -> Boolean): Stream<T> {
    var stopped = false
    return Stream {
        if (stopped) null
        else next().let {
            stopped = it == null || stop(it)
            if (stopped) null else it
        }
    }
}



/**
 * Returns a toStream consisting of the items of this toStream, until an item matching [stop] is
 * encountered. The matching item will be the last item of the returned toStream.
 */
inline fun <T : Any> Stream<T>.upThrough(crossinline stop: (T) -> Boolean): Stream<T> {
    var stopped = false
    return Stream { if (stopped) null else next() after { stopped = it == null || stop(it) } }
}



/**
 * Returns a toStream consisting of the items of this toStream, minus all the items at the beginning
 * of the toStream that match [drop].
 */
inline fun <T : Any> Stream<T>.dropWhile(crossinline drop: (T) -> Boolean): Stream<T> {
    var dropping = true
    return filter { !dropping || expr { dropping = drop(it); !dropping } }
}



/**
 * Returns a toStream consisting of the items of this toStream, minus its [n] first items.
 */
fun <T : Any> Stream<T>.drop(n: Int = 1): Stream<T> {
    var i = 0
    return dropWhile { (i < n) after { if (it) ++i } }
}



/**
 * Returns a toStream consisting of the items of this toStream, until an item that doesn't match
 * [keep] is encountered (this item will not be yielded by the returned toStream).
 */
inline fun <T : Any> Stream<T>.takeWhile(crossinline keep: (T) -> Boolean): Stream<T>
        = upTo { !keep(it) }



/**
 * Returns a toStream consisting of the items of this toStream, with a limit of [n].
 */
fun <T : Any> Stream<T>.limit(n: Int): Stream<T> {
    var i = 0
    return upTo { (i >= n) after { if (!it) ++i } }
}

/// [3] Other //////////////////////////////////////////////////////////////////////////////////////

/**
 * Returns a toStream consisting of the items of this toStream, paired with its index (starting
 * at 0 for the next item of this toStream).
 */
fun <T : Any> Stream<T>.indexed(): Stream<IndexedValue<T>> {
    var i = 0
    return Stream { next()?.let { IndexedValue(i++, it) } }
}



/**
 * Returns a toStream consisting of the distinct items of this toStream.
 *
 * Note that this streams holds on to a set of all items seen in the toStream!
 */
fun <T : Any> Stream<T>.distinct(): Stream<T>
        = distinctBy { it }



/**
 * Returns a toStream consisting of items whose selector (as returned by [selector]) are distinct.
 *
 * Note that this streams holds on to a set of all selectors seen in the toStream!
 */
inline fun <T : Any, K> Stream<T>.distinctBy(crossinline selector: (T) -> K): Stream<T> {
    val set = mutableSetOf<K>()
    return filter { set.add(selector(it)) }
}



/**
 * Returns a toStream consisting of pairs made up by one item of this toStream and one item of
 * [other]. The toStream only runs as far as the shortest of the two streams.
 */
infix fun <T : Any, U : Any> Stream<T>.zip(other: Stream<U>): Stream<Pair<T, U>> =
        Stream {
            val a = next()
            val b = other.next()
            if (a != null && b != null) Pair(a, b) else null
        }



/**
 * Returns a toStream consisting of pairs made up by one item of this toStream and one item of
 * [other]. The toStream runs as far as the longest of the two streams.
 */
infix fun <T : Any, U : Any> Stream<T>.ziplong(other: Stream<U>): Stream<Pair<T?, U?>> =
        Stream {
            val a = next()
            val b = other.next()
            if (a != null || b != null) Pair(a, b) else null
        }



/**
 * Return a toStream that is the concatenation of this toStream with [other].
 */
infix fun <T : Any> Stream<T>.then(other: Stream<T>): Stream<T> {
    var stream = this
    return Stream {
        stream.next() ?: if (stream === other) null else other.next().after { stream = other }
    }
}


