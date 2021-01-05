package org.mainsoft.base.screen.model.images

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.R
import org.mainsoft.base.lib.Action
import org.mainsoft.base.net.Repository
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class ImagesUseCase() : BaseApiUseCase() {

    fun getItems(state: ImagesViewState?, id: String): ReceiveChannel<Action<ImagesViewState>> = produceActions {
        send { copy(loading = true, error = null) }
        try {

            var isTopFirst = true
            val results = mutableListOf<Image>()
            state?.let {
                results.addAll(repository.getImages(id, state.page + 1))
                results.addAll(state.data)
                isTopFirst = state.isTopFirst
            }

            send { copy(data = results, loading = false, isTopFirst = isTopFirst) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false) }
            Log.e("ImagesUseCase", e.message, e)
        }
    }

    fun swipe(state: ImagesViewState?, mode: Int): ReceiveChannel<Action<ImagesViewState>> = produceActions {
        val data = state?.data

        val vote = when (mode) {
            R.id.offScreenPass,
            R.id.offScreenPassSecond -> 0
            else -> 1
        }

        data?.let {
            val idImage = data[data.size - 1].id
            data.removeAt(data.size - 1)

            val isTopFirst = when (mode) {
                R.id.offScreenPass,
                R.id.offScreenLike -> {
                    false
                }
                else -> {
                    true
                }
            }

            try {
                send { copy(data = data, loading = false, isTopFirst = isTopFirst) }
                repository.vote(idImage, vote)
            } catch (e: Exception) {
                Log.e("ImagesUseCase", e.message, e)
            }
        }
    }

}

