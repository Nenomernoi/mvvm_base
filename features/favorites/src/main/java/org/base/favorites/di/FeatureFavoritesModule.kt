package org.base.favorites.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.favorites.data.repository.FavoritesRepositoryImpl
import org.base.favorites.data_source.remote.FavoritesRemoteDtaSourceImpl
import org.base.favorites.data_source.remote.retrofit_service.FavoritesService
import org.base.favorites.db.data_source.FavoriteDbRepositoryImpl
import org.base.favorites.db.repository.FavoriteDbRepository
import org.base.favorites.domain.FavoritesRepository
import org.base.favorites.presentation.ui.favorites.FavoritesViewModel
import org.base.favorites.presentation.ui.favorites.processor.FavoritesProcessorHolder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureFavoritesModule = Kodein.Module(name = "FeatureFavoritesModule") {

    bind<FavoritesService>() with singleton { provideFavoritesService(retrofitFavorites = instance()) }

    bind<FavoritesRemoteDtaSourceImpl>() with singleton {
        FavoritesRemoteDtaSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceFavorites = instance()
        )
    }

    bind<FavoritesRepository>() with singleton {
        FavoritesRepositoryImpl(remoteDataSourceFavorites = instance(), mapperFavorites = instance())
    }

    bind<FavoriteDbRepository>() with singleton {
        FavoriteDbRepositoryImpl(
            ioDispatcher = instance(arg = "ioDispatcher"),
            daoFavorites = instance()
        )
    }

    bind<FavoritesProcessorHolder>() with singleton {
        FavoritesProcessorHolder(
            repositoryFavorites = instance(),
            repositoryDbFavorites = instance(),
            mapperFavorites = instance()
        )
    }

    bind<FavoritesViewModel>() with singleton { FavoritesViewModel(actionProcessorHolderFavorites = instance()) }
}

internal fun provideFavoritesService(
    retrofitFavorites: Retrofit
): FavoritesService = retrofitFavorites.create(FavoritesService::class.java)
