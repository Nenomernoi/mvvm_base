package by.nrstudio.ui_components.adapter.listeners

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun SwipeRefreshLayout.flowRefresh(): Flow<Unit> = callbackFlow {
    var listener: SwipeRefreshLayout.OnRefreshListener? = SwipeRefreshLayout.OnRefreshListener {
        isRefreshing = false
        trySend(Unit).isSuccess
    }

    setOnRefreshListener(listener)

    awaitClose {
        listener = null
        setOnRefreshListener(null)
    }
}

fun RecyclerView.flowEndless(): Flow<Int> = callbackFlow {

    var endLess: EndlessScrollListener? = object : EndlessScrollListener(this@flowEndless.layoutManager as LinearLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            if (this@flowEndless.adapter?.itemCount == 0) return
            trySend(page).isSuccess
        }
    }
    addOnScrollListener(endLess ?: return@callbackFlow)

    awaitClose {
        removeOnScrollListener(endLess ?: return@awaitClose)
        endLess = null
    }
}
