package by.nrstudio.mvi

import kotlinx.coroutines.flow.StateFlow

/**
 * Object that will subscribes to a [MviView]'s [MviIntent]s,
 * process it and emit a [MviViewState] back.
 *
 * @param I Top class of the [MviIntent] that the [MviViewModel] will be subscribing
 * to.
 * @param S Top class of the [MviViewState] the [MviViewModel] will be emitting.
 */
interface MviViewModel<I : MviIntent, A : MviAction, S : MviViewState> {

    fun processIntents(intent: I, vararg values: Any)

    fun mapIntentToAction(intent: I): A

    val uiState: StateFlow<S>
}
