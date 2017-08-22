package norswap.violin.stream

import norswap.violin.stream.Utils.testStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestPeekStream {

    @org.junit.Test fun invoke() {
        testStream { PeekStream(*it) }
        val stream = PeekStream(1, 2, 3)
        for (i in 1..3) {
            assertEquals(stream.peek(), i)
            assertEquals(stream.next(), i)
        }
        assertNull(stream.next())
        assertNull(PeekStream<Int>().next())
    }
}