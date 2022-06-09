package by.nrstudio.favorites.di

import by.nrstudio.favorites.data.repository.FavoritesRepositoryImpl
import by.nrstudio.favorites.data_source.remote.FavoritesRemoteDtaSourceImpl
import by.nrstudio.favorites.data_source.remote.retrofit_service.FavoritesService
import by.nrstudio.favorites.domain.FavoritesRepository
import by.nrstudio.favorites.presentation.ui.favorites.FavoritesViewModel
import by.nrstudio.favorites.presentation.ui.favorites.processor.FavoritesProcessorHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    bind<FavoritesProcessorHolder>() with singleton { FavoritesProcessorHolder(repositoryFavorites = instance(), mapperFavorites = instance()) }

    bind<FavoritesViewModel>() with singleton { FavoritesViewModel(actionProcessorHolderFavorites = instance()) }
}

internal fun provideFavoritesService(
    retrofitFavorites: Retrofit
): FavoritesService = retrofitFavorites.create(FavoritesService::class.java)
