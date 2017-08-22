package norswap.violin.stream

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import norswap.violin.stream.Utils.testStream

/**  */
class TestStream {

    /**  */
    @org.junit.Test fun empty() {
        assertNull(Stream.empty.next())
        assertNull(Stream.empty.next()) // empty can be reused
    }


    /**  */
    @org.junit.Test fun invokeArray() = testStream{ Stream(*it) }


    /**  */
    @org.junit.Test fun iterator() {
        val iter = Stream(1, 2, 3).iterator()
        for (i in 1..3) {
            assertTrue(iter.hasNext())
            assertEquals(iter.next(), i)
        }
        assertFalse(iter.hasNext())
//    assertThrows(NoSuchElementException::class.java) { iter.next() }
    }


    /**  */
    @org.junit.Test fun forLoop() {
        var i = 0
        for (j in arrayOf(1, 2, 3).toStream()) assertEquals(++i, j)
    }


    /**  */
//    @org.junit.Test fun toJavaStream() {
//        val stream = Stream(1, 2, 3).toJavaStream()
//        assertFalse(stream.isParallel)
//        var i = 0
//        stream.forEach { assertEquals(it, ++i) }
//    }


    /**  */
    @org.junit.Test fun transitive() {
        val list = 1.transitive { it + 1 }.limit(4).list()
        assertEquals(list, listOf(1, 2, 3, 4))
    }
}