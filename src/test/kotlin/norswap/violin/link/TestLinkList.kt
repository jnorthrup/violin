package norswap.violin.link

import norswap.violin.stream.each
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**  */
class TestLinkList {

    /**  */
    @org.junit.Test fun linkListEmpty() {
        val list = LinkList<Int>()
        assertEquals(list.size, 0)
        assertNull(list.stream().next())
        assertNull(list.linkStream().next())
        assertFalse(list.iterator().hasNext())
    }


    /**  */
    @org.junit.Test fun linkList() {
        val list = LinkList(1, 2, 3)
        var j = 0
        for (i in list)
            assertEquals(i, ++j)
        j = 0
        list.stream().each { assertEquals(it, ++j) }

    }


    /**  */
    @org.junit.Test fun linkListSize() {
        val list = LinkList(1, 2, 3)
        assertEquals(list.size, 3)
        list.pop()
        assertEquals(list.size, 2)
    }


    /**  */
    @org.junit.Test fun linkListStack() {
        val list = LinkList(1, 2, 3)
        assertEquals(list.pop(), 1)
        assertEquals(list.peek(), 2)
        list.push(0)
        assertEquals(list.peek(), 0)
        list.pop(); list.pop(); list.pop()
        assertTrue(list.empty)
        assertNull(list.peek())
        assertNull(list.pop())
    }


    /**  */
    @org.junit.Test fun linkListEquals() {
        val first = LinkList(1, 2, 3)
        val second = LinkList(first.link, 3)
        assertEquals(first, second)
        assertEquals(LinkList<Int>(), LinkList())
    }
}
