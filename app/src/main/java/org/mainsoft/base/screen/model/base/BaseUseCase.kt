package org.mainsoft.base.screen.model.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.mainsoft.base.lib.Action

abstract class BaseUseCase {

    @ExperimentalCoroutinesApi
    fun <T> produceActions(f: suspend ProducerScope<Action<T>>.() -> Unit): ReceiveChannel<Action<T>> =
            GlobalScope.produce(block = f)

    suspend fun <T> ProducerScope<Action<T>>.send(f: T.() -> T) = send(Action(f))
}