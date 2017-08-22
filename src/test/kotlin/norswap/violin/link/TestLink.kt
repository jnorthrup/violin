package norswap.violin.link

import norswap.violin.stream.each
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class TestLink {

    @org.junit.Test fun linkEmpty() {
        val link = Link<Int>()
        assertNull(link)
        assertNull(link.stream().next())
        assertNull(link.linkStream().next())
        assertFalse(link.iterator().hasNext())
    }


    @org.junit.Test fun link() {
        val link = Link(1, 2, 3)
        var j = 0
        for (i in link)
            assertEquals(i, ++j)
        j = 0
        link.stream().each { assertEquals(it, ++j) }

    }


    @org.junit.Test fun linkStream() {
        val link = Link(1, 2, 3)
        var i = 0
        link.linkStream().each { assertEquals(it.item, ++i) }
    }

}