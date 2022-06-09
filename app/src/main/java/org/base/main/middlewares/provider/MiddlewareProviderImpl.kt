package org.base.main.middlewares.provider

import org.base.network.middleware.NetworkMiddleware
import org.base.network.middleware.provider.MiddlewareProvider

class MiddlewareProviderImpl private constructor(
    private val middlewareList: List<NetworkMiddleware> = listOf()
) : MiddlewareProvider {

    class Builder(
        private val middlewareList: MutableList<NetworkMiddleware> = mutableListOf()
    ) {

        fun add(middleware: NetworkMiddleware) = apply {
            this.middlewareList.add(middleware)
        }

        fun build() = MiddlewareProviderImpl(
            middlewareList = middlewareList
        )
    }

    override fun getAll(): List<NetworkMiddleware> = middlewareList
}
