package org.base.breed.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breed.presentation.ui.breed.BreedViewModel
import org.base.breed.presentation.ui.breed.processor.BreedProcessorHolder
import org.base.breed_data.data.data_source.ImagesRepositoryImpl
import org.base.breed_data.data_source.remote.ImagesRemoteDataSourceImpl
import org.base.breed_data.data_source.remote.retrofit_service.ImagesService
import org.base.breed_data.db.data_source.ImageDbRepositoryImpl
import org.base.breed_data.db.repository.ImageDbRepository
import org.base.breed_data.domain.ImagesRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureBreedModule = DI.Module(name = "FeatureBreedModule") {

    bind<ImagesService>() with provider { provideImagesService(retrofitImages = instance()) }

    bind<ImagesRemoteDataSourceImpl>() with provider {
        ImagesRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceImages = instance()
        )
    }

    bind<ImagesRepository>() with provider {
        ImagesRepositoryImpl(remoteDataSourceImage = instance(), mapperImages = instance())
    }

    bind<ImageDbRepository>() with provider {
        ImageDbRepositoryImpl(
            ioDispatcher = instance(arg = "ioDispatcher"),
            daoImages = instance()
        )
    }

    bind<BreedProcessorHolder>() with provider {
        BreedProcessorHolder(
            repositoryFavorites = instance(),
            repositoryImages = instance(),
            repositoryDbFavorites = instance(),
            repositoryDbImages = instance(),
            repositoryDbBreeds = instance(),
            mapperFavorites = instance(),
            mapperBreed = instance(),
            mapperImages = instance(),
        )
    }

    bind<BreedViewModel>() with provider { BreedViewModel(actionProcessorHolderBreed = instance()) }
}

internal fun provideImagesService(
    retrofitImages: Retrofit
): ImagesService = retrofitImages.create(ImagesService::class.java)
