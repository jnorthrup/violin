package norswap.violin.stream

import org.junit.Assert.assertArrayEquals
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**  */
class TestConsumers {

    /**  */
    @org.junit.Test fun each() {
        var i = 0
        Stream(1, 2, 3).each { assertEquals(it, ++i) }
    }


    /**  */
    @org.junit.Test fun mutableList() {
        val list = Stream(1, 2, 3).mutableList()
        list.add(4)
        assertEquals(list, listOf(1, 2, 3, 4))
    }


    /**  */
    @org.junit.Test fun list() = assertEquals(Stream(1, 2, 3).list(), listOf(1, 2, 3))


    /**  */
    @org.junit.Test fun array() = assertArrayEquals(Stream(1, 2, 3).array(), arrayOf(1, 2, 3))


    /**  */
    @org.junit.Test fun foldl() = assertEquals(Stream(1, 2, 3).foldl(0) { a, b -> a * 10 + b }, 123)


    /**  */
    @org.junit.Test fun foldr() = assertEquals(Stream(1, 2, 3).foldr(0) { a, b -> a * 10 + b }, 321)


    /**  */
    @org.junit.Test fun reduce() {
        assertEquals(Stream(1, 2, 3).reduce { a, b -> a * 10 + b }, 123)
        assertNull(Stream<Int>().reduce { a, b -> a + b })
    }


    /**  */
    @org.junit.Test fun reduceRight() {
        assertEquals(Stream(1, 2, 3).reduceRight { a, b -> a * 10 + b }, 321)
        assertNull(Stream<Int>().reduceRight { a, b -> a + b })
    }


    /**  */
    @org.junit.Test fun last() {
        assertEquals(Stream(1, 2, 3).last(), 3)
        assertNull(Stream<Int>().last())
    }


    /**  */
    @org.junit.Test fun any() {
        assertTrue(Stream(1, 2, 3).any { it % 2 == 0 })
        assertFalse(Stream(1, 3, 5).any { it % 2 == 0 })
        assertFalse(Stream<Int>().any { true })
    }


    /**  */
    @org.junit.Test fun all() {
        assertTrue(Stream(1, 2, 3).all { it < 4 })
        assertFalse(Stream(1, 2, 3, 4).all { it < 4 })
        assertTrue(Stream<Int>().all { true })
    }


    /**  */
    @org.junit.Test fun count() {
        assertEquals(Stream(1, 2, 3).count(), 3)
        assertEquals(Stream<Int>().count(), 0)
    }


    /**  */
    @org.junit.Test fun max() {
        assertEquals(Stream(1, 3, 2).max(), 3)
        assertEquals(Stream(1, 2, 3).max(), 3)
        assertEquals(Stream(3, 2, 1).max(), 3)
        assertNull(Stream<Int>().max())
    }

    /**  */
    val intComparator = Comparator<Int> { a, b -> a - b }


    /**  */
    @org.junit.Test fun maxWith() = assertEquals(Stream(1, 2, 3).maxWith(intComparator), 3)


    /**  */
    @org.junit.Test fun min() {
        assertEquals(Stream(3, 1, 2).min(), 1)
        assertEquals(Stream(1, 2, 3).min(), 1)
        assertEquals(Stream(3, 2, 1).min(), 1)
        assertNull(Stream<Int>().min())
    }


    /**  */
    @org.junit.Test fun minWith() = assertEquals(Stream(1, 2, 3).minWith(intComparator), 1)


    /**  */
    @org.junit.Test fun associate() = assertEquals(Stream(1, 2, 3).associate { it to it }, mapOf(1 to 1, 2 to 2, 3 to 3))


    /**  */
    @org.junit.Test fun linkList() {
        var i = 3
        Stream(1, 2, 3).linkList().stream().each { assertEquals(it, i--) }
    }


    /**  */
    @org.junit.Test fun set() {
        val set = Stream(1, 2, 3).set()
        assertEquals(set, setOf(1, 2, 3))
    }


    /**  */
    @org.junit.Test fun joinToString() {
        val str = Stream(1, 2, 3)
                .joinToString(prefix = "[", postfix = "]", limit = 2) { (it * 2).toString() }
        assertEquals(str, "[2, 4, ...]")
    }


    /**  */
    @org.junit.Test fun groupBy() {
        val map = Stream(2, 3, 4, 5, 6, 7).groupBy { it / 2 }
        val expect = mapOf(1 to listOf(2, 3), 2 to listOf(4, 5), 3 to listOf(6, 7))
        assertEquals(map, expect)
    }


    /**  */
    @org.junit.Test fun partition() {
        val pair = Stream(1, 2, 3, 4).partition { it % 2 == 0 }
        val expect = Pair(listOf(2, 4), listOf(1, 3))
        assertEquals(pair, expect)
    }
}