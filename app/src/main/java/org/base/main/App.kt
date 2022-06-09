package org.base.main

import android.app.Application
import middleWareModule
import networkModule
import org.base.breeds.di.featureBreedsModule
import org.base.favorites.di.featureFavoritesModule
import org.base.main.di.appModule
import org.base.main.di.commonModelsModule
import org.base.main.di.coroutinesModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class App : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@App))
        import(appModule)
        import(coroutinesModule)
        import(networkModule)
        import(middleWareModule)
        import(commonModelsModule)

        import(featureFavoritesModule)
        import(featureBreedsModule)
    }
}
