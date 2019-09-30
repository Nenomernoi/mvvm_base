package org.mainsoft.base.screen.model.breeds

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class BreedsUseCase(repository: Repository) : BaseApiUseCase(repository) {

    @ExperimentalCoroutinesApi
    fun getList(state: BreedsViewState?, page: Int): ReceiveChannel<Action<BreedsViewState>> = produceActions {
        send { copy(loading = page == 0, error = null, page = page, model = null) }
        try {

            val results = state?.data ?: mutableListOf()
            results.addAll(repository.getBreeds(page))

            send { copy(data = results, loading = false, page = page, model = null) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false, page = page, model = null) }
            Log.e("BreedsUseCase", e.message, e)
        }
    }

    suspend fun addToFavorite(state: BreedsViewState, position: Int): Action<BreedsViewState> {
        val newBreed = repository.addFavorite(state.data[position])
        return Action {
            data[position] = newBreed
            copy(data = data, model = null)
        }
    }

    fun openItem(position: Int, originalPos: IntArray): Action<BreedsViewState> {
        return Action {
            val model = data[position]
            copy(position = position, originalPos = originalPos, model = model)
        }
    }

    suspend fun updateItem(state: BreedsViewState, position: Int): Action<BreedsViewState> {
        val item = state.data[position]
        val newBreed = repository.getBreed(item.id)

        return Action {
            data[position] = newBreed
            copy(data = data, model = null)
        }
    }

    @ExperimentalCoroutinesApi
    fun clearData(): ReceiveChannel<Action<BreedsViewState>> = produceActions {
        send { copy(data = mutableListOf(), refresh = false, loading = true, error = null, page = 0, model = null) }
        try {
            repository.clearData()
            val breeds = repository.getBreeds(0)
            send { copy(data = breeds.toMutableList(), loading = false, page = page, model = null) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false, page = page, model = null) }
            Log.e("BreedsUseCase", e.message, e)
        }
    }

}

