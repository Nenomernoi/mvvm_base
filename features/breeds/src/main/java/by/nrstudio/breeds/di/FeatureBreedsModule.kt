package by.nrstudio.breeds.di

import by.nrstudio.breeds.data.repository.BreedsRepositoryImpl
import by.nrstudio.breeds.data_source.remote.BreedsRemoteDataSourceImpl
import by.nrstudio.breeds.data_source.remote.retrofit_service.BreedsService
import by.nrstudio.breeds.domain.BreedsRepository
import by.nrstudio.breeds.presentation.ui.breeds.BreedsViewModel
import by.nrstudio.breeds.presentation.ui.breeds.processor.BreedsProcessorHolder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val featureBreedsModule = Kodein.Module(name = "FeatureBreedsModule") {

    bind<BreedsService>() with singleton { provideBreedsService(retrofit = instance()) }

    bind<BreedsRemoteDataSourceImpl>() with singleton {
        BreedsRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            movieService = instance()
        )
    }

    bind<BreedsRepository>() with singleton {
        BreedsRepositoryImpl(remoteDataSource = instance(), mapper = instance())
    }

    bind<BreedsProcessorHolder>() with singleton { BreedsProcessorHolder(repository = instance(), mapper = instance()) }

    bind<BreedsViewModel>() with singleton { BreedsViewModel(actionProcessorHolder = instance()) }
}

internal fun provideBreedsService(
    retrofit: Retrofit
): BreedsService = retrofit.create(BreedsService::class.java)
