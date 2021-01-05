package org.mainsoft.base.screen.model.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.generic.instance
import org.mainsoft.base.App
import org.mainsoft.base.net.Repository

abstract class BaseViewModel : ViewModel() {

    companion object {
        const val ARGUMENT_ID = "argument_id"
        const val ARGUMENT_EXTRA = "argument_extra"
        const val ARGUMENT_RETURN = "argument_return"
    }

    protected lateinit var store: CoroutineScope

    fun <V : Any> getStore(): V = store as V

    override fun onCleared() {
        store.cancel()
    }

    abstract fun loadData()
    abstract fun getState(): Any
}