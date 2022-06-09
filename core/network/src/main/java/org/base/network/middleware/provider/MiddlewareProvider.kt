package org.base.network.middleware.provider

import org.base.network.middleware.NetworkMiddleware

interface MiddlewareProvider {
    fun getAll(): List<NetworkMiddleware>
}
