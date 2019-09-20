package org.mainsoft.base.screen.model.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected lateinit var store: CoroutineScope

    fun <V : Any> getStore(): V = store as V

    override fun onCleared() {
        store.cancel()
    }

    abstract fun loadData()
}