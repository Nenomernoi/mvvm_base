package org.base.main.functional_programming.utils

import org.base.main.functional_programming.Either

fun <R> R.toSuccess(): Either.Success<R> {
    return Either.Success(this)
}

fun <L> L.toError(): Either.Error<L> {
    return Either.Error(this)
}
