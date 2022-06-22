package org.base.breeds.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breeds.presentation.ui.breeds.BreedsViewModel
import org.base.breeds.presentation.ui.breeds.processor.BreedsProcessorHolder
import org.base.breeds_data.data.data_source.BreedsRepositoryImpl
import org.base.breeds_data.data_source.remote.BreedsRemoteDataSourceImpl
import org.base.breeds_data.data_source.remote.retrofit_service.BreedsService
import org.base.breeds_data.db.data_source.BreedsDbRepositoryImpl
import org.base.breeds_data.db.repository.BreedsDbRepository
import org.base.breeds_data.domain.BreedsRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureBreedsModule = DI.Module(name = "FeatureBreedsModule") {

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
