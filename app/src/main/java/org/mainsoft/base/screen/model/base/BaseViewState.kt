package org.mainsoft.base.screen.model.base

abstract class BaseViewState<T> {
    abstract val data: T?
    abstract val loading: Boolean
    abstract val error: Throwable?
}

abstract class BaseListState<T>(
        override val data: MutableList<T> = mutableListOf(),
        override val loading: Boolean = false,
        override val error: Throwable? = null
) : BaseViewState<MutableList<T>>() {
    abstract val page: Int
}

abstract class BaseRefreshListState<T>(
        override val data: MutableList<T> = mutableListOf(),
        override val page: Int = 0,
        override val loading: Boolean = false,
        override val error: Throwable? = null
) : BaseListState<T>() {
    abstract val refresh: Boolean
}
