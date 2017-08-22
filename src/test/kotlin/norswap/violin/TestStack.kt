package norswap.violin

import norswap.violin.link.LinkList
import norswap.violin.stream.Utils.testStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestStack {
    @org.junit.Test fun poppingStream() {
        testStream { LinkList(*it).poppingStream() }
        val stack = LinkList(1, 2, 3)
        val stream = stack.poppingStream()
        assertEquals(stream.next(), 1)
        assertEquals(stack.peek(), 2)
        assertEquals(stream.peek(), 2)
        assertEquals(stack.peek(), 2)
        assertEquals(stream.next(), 2)
        assertEquals(stack.pop(), 3)
        assertNull(stream.next(), null)
    }
}