package durdinstudios.wowarena.misc

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable


/** Take the first element that matches the filter function. */
inline fun <T> Observable<T>.filterOne(crossinline fn: (T) -> Boolean): Maybe<T> {
    return filter { fn(it) }.take(1).singleElement()
}

/** Take the first element that matches the filter function. */
inline fun <T> Flowable<T>.filterOne(crossinline fn: (T) -> Boolean): Maybe<T> {
    return filter { fn(it) }.take(1).singleElement()
}