package org.base.main

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breed.di.featureBreedModule
import org.base.breeds.di.featureBreedsModule
import org.base.di.modules.appModule
import org.base.di.modules.commonModelsModule
import org.base.di.modules.coroutinesModule
import org.base.di.modules.dbModule
import org.base.di.modules.middleWareModule
import org.base.di.modules.networkModule
import org.base.favorites.di.featureFavoritesModule
import org.base.ui_components.BaseApp
import org.kodein.di.DI
import org.kodein.di.android.x.androidXModule

@ExperimentalCoroutinesApi
class App : BaseApp() {

    override fun setUpDI() {
        di = DI.lazy {
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
}
