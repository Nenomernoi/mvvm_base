package org.base.network

import org.base.network.middleware.NetworkMiddleware
import org.base.network.models.exception.NetworkMiddlewareFailure

class DumbMiddleware(
    private val hardCodedValidation: Boolean,
    private val middlewareFailureMessage: String
) : NetworkMiddleware() {

    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure(middleWareExceptionMessage = middlewareFailureMessage)

    override fun isValid(): Boolean = hardCodedValidation
}

class AnotherDumbMiddleware() : NetworkMiddleware() {
    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure("")

    override fun isValid(): Boolean = true
}
