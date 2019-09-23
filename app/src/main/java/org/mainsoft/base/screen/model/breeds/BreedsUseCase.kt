package org.mainsoft.base.screen.model.breeds

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.screen.model.base.BaseUseCase

class BreedsUseCase(repository: Repository) : BaseUseCase(repository) {

    @ExperimentalCoroutinesApi
    fun getList(state: BreedsViewState?, page: Int): ReceiveChannel<Action<BreedsViewState>> = produceActions {
        send { copy(loading = page == 0, error = null, page = page) }
        try {

            val results = state?.data ?: mutableListOf()
            results.addAll(repository.getBreeds(page))

            send { copy(data = results, loading = false, page = page) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false, page = page) }
            Log.e("BreedsUseCase", e.message, e)
        }
    }

    suspend fun addToFavorite(state: BreedsViewState, position: Int): Action<BreedsViewState> {
        val newBreed = repository.showFull(state.data[position])
        return Action {
            data[position] = newBreed
            copy(data = data)
        }
    }


    @ExperimentalCoroutinesApi
    fun clearData(): ReceiveChannel<Action<BreedsViewState>> = produceActions {
        send { copy(data = mutableListOf(), refresh = false, loading = true, error = null, page = 0) }
        try {
            repository.clearData()
            val breeds = repository.getBreeds(0)
            send { copy(data = breeds.toMutableList(), loading = false, page = page) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false, page = page) }
            Log.e("BreedsUseCase", e.message, e)
        }
    }

}

