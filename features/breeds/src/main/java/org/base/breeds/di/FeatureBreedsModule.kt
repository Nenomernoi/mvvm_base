package org.base.breeds.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breeds.data.data_source.BreedsRepositoryImpl
import org.base.breeds.data_source.remote.BreedsRemoteDataSourceImpl
import org.base.breeds.data_source.remote.retrofit_service.BreedsService
import org.base.breeds.db.data_source.BreedsDbRepositoryImpl
import org.base.breeds.db.repository.BreedsDbRepository
import org.base.breeds.domain.BreedsRepository
import org.base.breeds.presentation.ui.breeds.BreedsViewModel
import org.base.breeds.presentation.ui.breeds.processor.BreedsProcessorHolder
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

    bind<BreedsDbRepository>() with singleton {
        BreedsDbRepositoryImpl(
            ioDispatcher = instance(arg = "ioDispatcher"),
            daoBreeds = instance()
        )
    }

    bind<BreedsProcessorHolder>() with singleton {
        BreedsProcessorHolder(
            repositoryBreeds = instance(),
            repositoryDbBreeds = instance(),
            mapperBreeds = instance()
        )
    }

    bind<BreedsViewModel>() with singleton { BreedsViewModel(actionProcessorHolderBreeds = instance()) }
}

internal fun provideBreedsService(
    retrofitBreeds: Retrofit
): BreedsService = retrofitBreeds.create(BreedsService::class.java)
