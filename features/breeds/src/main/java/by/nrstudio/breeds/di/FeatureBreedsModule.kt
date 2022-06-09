package by.nrstudio.breeds.di

import by.nrstudio.breeds.data.repository.BreedsRepositoryImpl
import by.nrstudio.breeds.data_source.remote.BreedsRemoteDataSourceImpl
import by.nrstudio.breeds.data_source.remote.retrofit_service.BreedsService
import by.nrstudio.breeds.domain.BreedsRepository
import by.nrstudio.breeds.presentation.ui.breeds.BreedsViewModel
import by.nrstudio.breeds.presentation.ui.breeds.processor.BreedsProcessorHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureBreedsModule = Kodein.Module(name = "FeatureBreedsModule") {

    bind<BreedsService>() with singleton { provideBreedsService(retrofitBreeds = instance()) }

    bind<BreedsRemoteDataSourceImpl>() with singleton {
        BreedsRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceBreeds = instance()
        )
    }

    bind<BreedsRepository>() with singleton {
        BreedsRepositoryImpl(remoteDataSourceBreeds = instance(), mapperBreeds = instance())
    }

    bind<BreedsProcessorHolder>() with singleton { BreedsProcessorHolder(repositoryBreeds = instance(), mapperBreeds = instance()) }

    bind<BreedsViewModel>() with singleton { BreedsViewModel(actionProcessorHolderBreeds = instance()) }
}

internal fun provideBreedsService(
    retrofitBreeds: Retrofit
): BreedsService = retrofitBreeds.create(BreedsService::class.java)
