package by.nrstudio.ui_components.ui

import by.nrstudio.basemodulesmvi.functional_programming.Failure
import by.nrstudio.mvi.MviViewState
import by.nrstudio.mvi.Status
import by.nrstudio.utils.OneTimeEvent

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
