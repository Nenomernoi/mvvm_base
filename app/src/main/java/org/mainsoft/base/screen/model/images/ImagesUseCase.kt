package org.mainsoft.base.screen.model.images

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class ImagesUseCase(repository: Repository) : BaseApiUseCase(repository) {

    fun getItems(state: ImagesViewState?, id: String): ReceiveChannel<Action<ImagesViewState>> = produceActions {
        send { copy(loading = true, error = null) }
        try {

            val results = state?.data ?: mutableListOf()
            results.addAll(repository.getImages(id, (state?.page ?: 0) + 1))

            send { copy(data = results, loading = false) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false) }
            Log.e("ImagesUseCase", e.message, e)
        }
    }


    suspend fun swipe(state: ImagesViewState): Action<ImagesViewState> {
        return Action {
            data.removeAt(data.size - 1)
            copy(data = data, loading = false)
        }
    }

}

