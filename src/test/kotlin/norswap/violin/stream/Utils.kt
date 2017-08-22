package norswap.violin.stream

import kotlin.test.assertEquals
import kotlin.test.assertNull

/**  */
object Utils {
   /**  */
   fun testStream(builder: (Array<Int>) -> Stream<Int>) {
        val stream = builder(arrayOf(1, 2, 3))
        for (i in 1..3) assertEquals(stream.next(), i)
        assertNull(stream.next())
        assertNull(builder(arrayOf()).next())
    }
}