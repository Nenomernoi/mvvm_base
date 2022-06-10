package org.base.breed.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.breed.data_source.remote.ImagesRemoteDataSourceImpl
import org.base.breed.data_source.remote.retrofit_service.ImagesService
import org.base.breed.db.data_source.ImageDbRepositoryImpl
import org.base.breed.db.repository.ImageDbRepository
import org.base.breed.presentation.ui.breed.processor.BreedProcessorHolder
import org.base.favorites.data.data_source.FavoritesRepositoryImpl
import org.base.favorites.domain.FavoritesRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureBreedModule = Kodein.Module(name = "FeatureBreedModule") {

    bind<ImagesService>() with singleton { provideFavoritesService(retrofitFavorites = instance()) }

    bind<ImagesRemoteDataSourceImpl>() with singleton {
        ImagesRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceImages = instance()
        )
    }

    bind<FavoritesRepository>() with singleton {
        FavoritesRepositoryImpl(remoteDataSourceFavorites = instance(), mapperFavorites = instance())
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
            mapperFavorites = instance(),
            mapperImages = instance(),
        )
    }

    // TODO bind<FavoritesViewModel>() with singleton { FavoritesViewModel(actionProcessorHolderFavorites = instance()) }
}

internal fun provideFavoritesService(
    retrofitFavorites: Retrofit
): ImagesService = retrofitFavorites.create(ImagesService::class.java)
