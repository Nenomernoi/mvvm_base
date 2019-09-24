package org.mainsoft.base.screen.model.breed

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class BreedUseCase(repository: Repository) : BaseApiUseCase(repository) {

    fun getItem(id: String): ReceiveChannel<Action<BreedViewState>> = produceActions {
        send { copy(loading = true, error = null) }
        try {
            val it = repository.getBreed(id)
            send { copy(data = it, loading = false) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false) }
            Log.e("BreedUseCase", e.message, e)
        }
    }

    fun setModel(model: Breed?): ReceiveChannel<Action<BreedViewState>> = produceActions {
        send { copy(loading = true, error = null) }
        send { copy(data = model, loading = false) }
    }

    fun addToFavorite(state: BreedViewState): ReceiveChannel<Action<BreedViewState>> = produceActions {
        val newBreed = repository.addFavorite(state.data!!)
        send { copy(update = true) }
        send { copy(data = newBreed, update = false) }
    }
}

