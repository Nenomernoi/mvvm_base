package org.base.favorites.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.favorites.presentation.ui.favorites.FavoritesViewModel
import org.base.favorites.presentation.ui.favorites.processor.FavoritesProcessorHolder
import org.base.favorites_data.data.data_source.FavoritesRepositoryImpl
import org.base.favorites_data.data_source.remote.FavoritesRemoteDataSourceImpl
import org.base.favorites_data.data_source.remote.retrofit_service.FavoritesService
import org.base.favorites_data.db.data_source.FavoriteDbRepositoryImpl
import org.base.favorites_data.db.repository.FavoriteDbRepository
import org.base.favorites_data.domain.FavoritesRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val featureFavoritesModule = DI.Module(name = "FeatureFavoritesModule") {

    bind<FavoritesService>() with provider { provideFavoritesService(retrofitFavorites = instance()) }

    bind<FavoritesRemoteDataSourceImpl>() with provider {
        FavoritesRemoteDataSourceImpl(
            middlewareProvider = instance(),
            ioDispatcher = instance(arg = "ioDispatcher"),
            adapter = instance(),
            serviceFavorites = instance()
        )
    }

    bind<FavoritesRepository>() with provider {
        FavoritesRepositoryImpl(remoteDataSourceFavorites = instance(), mapperFavorites = instance())
    }

    bind<FavoriteDbRepository>() with provider {
        FavoriteDbRepositoryImpl(
            ioDispatcher = instance(arg = "ioDispatcher"),
            daoFavorites = instance()
        )
    }

    bind<FavoritesProcessorHolder>() with provider {
        FavoritesProcessorHolder(
            repositoryFavorites = instance(),
            repositoryDbFavorites = instance(),
            mapperFavorites = instance()
        )
    }

    bind<FavoritesViewModel>() with provider { FavoritesViewModel(actionProcessorHolderFavorites = instance()) }
}

internal fun provideFavoritesService(
    retrofitFavorites: Retrofit
): FavoritesService = retrofitFavorites.create(FavoritesService::class.java)
