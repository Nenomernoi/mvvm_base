package by.nrstudio.basemodulesmvi

import android.app.Application
import by.nrstudio.basemodulesmvi.di.appModule
import by.nrstudio.basemodulesmvi.di.commonModelsModule
import by.nrstudio.basemodulesmvi.di.coroutinesModule
import by.nrstudio.breeds.di.featureBreedsModule
import by.nrstudio.favorites.di.featureFavoritesModule
import middleWareModule
import networkModule
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
