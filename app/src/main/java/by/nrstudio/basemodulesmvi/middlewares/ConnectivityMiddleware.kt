package by.nrstudio.basemodulesmvi.middlewares

import by.nrstudio.basemodulesmvi.R
import by.nrstudio.utils.connectivity.ConnectivityUtils
import by.nrstudio.network.middleware.NetworkMiddleware
import by.nrstudio.network.models.exception.NetworkMiddlewareFailure
import by.nrstudio.utils.resource_provider.ResourceProvider

class ConnectivityMiddleware(
    private val connectivityUtils: ConnectivityUtils,
    private val resourceProvider: ResourceProvider
) : NetworkMiddleware() {

    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure(
            middleWareExceptionMessage = resourceProvider.getString(R.string.error_no_network_detected)
        )

    override fun isValid(): Boolean = connectivityUtils.isNetworkAvailable()
}
