package org.mainsoft.base.screen.model.images

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class ImagesUseCase(repository: Repository) : BaseApiUseCase(repository) {

    fun getItems(state: ImagesViewState?, id: String): ReceiveChannel<Action<ImagesViewState>> = produceActions {
        send { copy(loading = true, error = null) }
        try {

            val results = mutableListOf<Image>()
            state?.let {
                results.addAll(repository.getImages(id, state.page + 1))
                results.addAll(state.data)
            }

            send { copy(data = results, loading = false) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false) }
            Log.e("ImagesUseCase", e.message, e)
        }
    }

    fun swipe(state: ImagesViewState?, vote: Boolean): ReceiveChannel<Action<ImagesViewState>> = produceActions {
        val data = state?.data
        data?.let {
            val idImage = data[data.size - 1].id
            data.removeAt(data.size - 1)
            try {
                send { copy(data = data, loading = false) }
                repository.vote(idImage, if (vote) 1 else 0)
            } catch (e: Exception) {
                 Log.e("ImagesUseCase", e.message, e)
            }
        }
    }

}

