package org.base.main

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import middleWareModule
import org.base.breed.di.featureBreedModule
import org.base.breeds.di.featureBreedsModule
import org.base.favorites.di.featureFavoritesModule
import org.base.main.di.appModule
import org.base.main.di.commonModelsModule
import org.base.main.di.coroutinesModule
import org.base.main.di.dbModule
import org.base.main.di.networkModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

@ExperimentalCoroutinesApi
class App : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@App))
        import(appModule)
        import(coroutinesModule)
        import(networkModule)
        import(middleWareModule)
        import(commonModelsModule)
        import(dbModule)

        import(featureFavoritesModule)
        import(featureBreedsModule)
        import(featureBreedModule)
    }
}
