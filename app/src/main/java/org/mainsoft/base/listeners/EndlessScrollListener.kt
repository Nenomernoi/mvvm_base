package org.mainsoft.base.listeners

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener : RecyclerView.OnScrollListener {

    private var visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    private var showHide = true

    internal var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            }
            if (i != 0 && lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        if (dy > 0 || dy < 0 && showHide) {
            showHide = false
            // onShowHideFab(showHide)
        }

        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager)
                        .findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition = (mLayoutManager as GridLayoutManager)
                    .findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
        }

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            showHide = true
            //  onShowHideFab(showHide)
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    fun setCurrentPage(page: Int, previousTotalItemCount: Int) {
        this.currentPage = page
        this.previousTotalItemCount = previousTotalItemCount
        this.loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
    //  abstract fun onShowHideFab(isShowHide: Boolean)

}
