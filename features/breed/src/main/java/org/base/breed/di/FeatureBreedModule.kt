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
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureBreedModule = Kodein.Module(name = "FeatureBreedModule") {

    bind<ImagesService>() with singleton { provideImagesService(retrofitImages = instance()) }

    bind<ImagesRemoteDataSourceImpl>() with singleton {
        ImagesRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceImages = instance()
        )
    }

    bind<ImagesRepository>() with singleton {
        ImagesRepositoryImpl(remoteDataSourceImage = instance(), mapperImages = instance())
    }

    bind<ImageDbRepository>() with singleton {
        ImageDbRepositoryImpl(
            ioDispatcher = instance(arg = "ioDispatcher"),
            daoImages = instance()
        )
    }

    bind<BreedProcessorHolder>() with singleton {
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

    bind<BreedViewModel>() with singleton { BreedViewModel(actionProcessorHolderBreed = instance()) }
}

internal fun provideImagesService(
    retrofitImages: Retrofit
): ImagesService = retrofitImages.create(ImagesService::class.java)
