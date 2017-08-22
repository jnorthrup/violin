package norswap.violin

/**
 * See [Maybe].
 */
class Some<out T : Any>(val value: T) : Maybe<T>()