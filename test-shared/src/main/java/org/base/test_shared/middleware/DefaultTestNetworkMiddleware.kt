package org.base.test_shared.middleware

import org.base.network.middleware.NetworkMiddleware
import org.base.network.models.exception.NetworkMiddlewareFailure

class DefaultTestNetworkMiddleware(
    private val isMiddlewareValid: Boolean,
    private val failureMessage: String = ""
) : NetworkMiddleware() {

    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure(middleWareExceptionMessage = failureMessage)

    override fun isValid(): Boolean = isMiddlewareValid
}
