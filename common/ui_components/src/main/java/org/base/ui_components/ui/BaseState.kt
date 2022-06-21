package org.base.ui_components.ui

import org.base.functional_programming.Failure
import org.base.mvi.MviViewState
import org.base.mvi.Status
import org.base.utils.OneTimeEvent

abstract class BaseViewState : MviViewState {
    abstract val status: Status
    abstract val error: OneTimeEvent<Failure>?
}

abstract class BaseItemViewState<T> : BaseViewState() {
    abstract val data: T
    override val status: Status = Status.NONE
    override val error: OneTimeEvent<Failure>? = null
}

abstract class BaseListViewState<T> : BaseItemViewState<MutableList<T>>() {
    override val data: MutableList<T> = mutableListOf()
    override val status: Status = Status.NONE
    override val error: OneTimeEvent<Failure>? = null
    abstract val page: Int
}
