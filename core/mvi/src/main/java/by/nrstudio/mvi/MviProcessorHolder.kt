package by.nrstudio.mvi

import kotlinx.coroutines.flow.Flow

interface MviProcessorHolder<A : MviAction, R : MviResult> {

    fun processAction(action: A, vararg values: Any): Flow<R>
}
