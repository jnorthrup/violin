package norswap.violin.stream

import norswap.violin.stream.Utils.testStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestCompat {

    @org.junit.Test fun Iterator_stream() {
        testStream { it.iterator().stream() }
    }


    @org.junit.Test fun Stream_stream() {
         testStream { java.util.stream.Stream.of(*it).stream() }
    }


    @org.junit.Test fun Sequence_stream() {
        testStream { sequenceOf(*it).stream() }
    }


    @org.junit.Test fun Array_stream() {
        testStream { it.stream() }
    }


    @org.junit.Test fun Array_reverseStream() {
        val stream = arrayOf(3, 2, 1).reverseStream()
        for (i in 1..3) assertEquals(stream.next(), i)
      assertNull(stream.next())
        assertNull(arrayOf<Int>().reverseStream().next())
    }


    @org.junit.Test fun Array_pureStream() {
        val stream = arrayOf(null, null, 1, null, null, 2, null, null, 3, null, null).pureStream()
        for (i in 1..3) assertEquals(stream.next(), i)
        assertNull(stream.next())
        assertNull(arrayOf<Int>().pureStream().next())
        assertNull(arrayOf<Int?>(null, null).pureStream().next())
    }


    @org.junit.Test fun Array_pureReverseStream() {
        val stream = arrayOf(null, null, 3, null, null, 2, null, null, 1, null, null).pureReverseStream()
        for (i in 1..3) assertEquals(stream.next(), i)
        assertNull(stream.next())
        assertNull(arrayOf<Int>().pureReverseStream().next())
        assertNull(arrayOf<Int?>(null, null).pureReverseStream().next())
    }
}
