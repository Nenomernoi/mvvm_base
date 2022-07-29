package org.base.di.modules

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.di.util.connectivity.ConnectivityUtilsImpl
import org.base.di.util.resource_provider.ResourceProviderImpl
import org.base.ui_components.BaseApp
import org.base.utils.connectivity.ConnectivityUtils
import org.base.utils.resource_provider.ResourceProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.multiton
import org.kodein.di.singleton

@ExperimentalCoroutinesApi
val appModule = DI.Module(name = "AppModule") {
    bind<Context>() with multiton { app: BaseApp ->
        app.applicationContext
    }
    bind<ResourceProvider>() with singleton { ResourceProviderImpl(instance()) }
    bind<ConnectivityUtils>() with singleton { ConnectivityUtilsImpl(instance()) }
}
