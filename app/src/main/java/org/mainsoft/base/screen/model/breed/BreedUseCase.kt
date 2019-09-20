package org.mainsoft.base.screen.model.breed

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.screen.model.base.BaseUseCase

class BreedUseCase(repository: Repository) : BaseUseCase(repository) {

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
}

