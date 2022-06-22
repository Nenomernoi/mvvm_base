import org.base.main.middlewares.ConnectivityMiddleware
import org.base.main.middlewares.provider.MiddlewareProviderImpl
import org.base.network.middleware.provider.MiddlewareProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val middleWareModule = DI.Module(name = "MiddleWareModule") {
    bind<MiddlewareProvider>() with singleton {
        MiddlewareProviderImpl.Builder()
            .add(
                middleware = ConnectivityMiddleware(
                    connectivityUtils = instance(),
                    resourceProvider = instance()
                )
            )
            .build()
    }
}
