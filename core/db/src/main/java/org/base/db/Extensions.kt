package org.base.db

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.base.functional_programming.Either
import org.base.functional_programming.Failure
import org.base.functional_programming.utils.toError
import org.base.functional_programming.utils.toSuccess

suspend inline fun <T> call(
    ioDispatcher: CoroutineDispatcher,
    crossinline dbCall: suspend () -> T
): Either<Failure, T> {
    return executeDbCall(ioDispatcher, dbCall)
}

suspend inline fun <T> executeDbCall(
    ioDispatcher: CoroutineDispatcher,
    crossinline dbCall: suspend () -> T
): Either<Failure, T> {
    return withContext(ioDispatcher) {
        try {
            return@withContext dbCall().toSuccess()
        } catch (e: Exception) {
            return@withContext Failure.UnexpectedFailure("Db fail").toError()
        }
    }
}
