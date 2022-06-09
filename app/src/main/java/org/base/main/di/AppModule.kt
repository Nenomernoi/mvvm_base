package org.base.main.di

import android.content.Context
import org.base.main.App
import org.base.main.util.connectivity.ConnectivityUtilsImpl
import org.base.main.util.resource_provider.ResourceProviderImpl
import org.base.utils.connectivity.ConnectivityUtils
import org.base.utils.resource_provider.ResourceProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton

val appModule = Kodein.Module(name = "AppModule") {
    bind<Context>() with multiton { app: App ->
        app.applicationContext
    }
    bind<ResourceProvider>() with singleton { ResourceProviderImpl(instance()) }
    bind<ConnectivityUtils>() with singleton { ConnectivityUtilsImpl(instance()) }
}
