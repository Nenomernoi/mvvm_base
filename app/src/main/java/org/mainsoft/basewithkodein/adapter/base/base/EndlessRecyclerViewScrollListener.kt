package org.mainsoft.basewithkodein.base

abstract class EndlessRecyclerViewScrollListener : androidx.recyclerview.widget.RecyclerView.OnScrollListener {

    private var visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    internal var mLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    constructor(layoutManager: androidx.recyclerview.widget.LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: androidx.recyclerview.widget.GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        when (mLayoutManager) {
            is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (mLayoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager)
                        .findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is androidx.recyclerview.widget.GridLayoutManager -> lastVisibleItemPosition = (mLayoutManager as androidx.recyclerview.widget.GridLayoutManager)
                    .findLastVisibleItemPosition()
            is androidx.recyclerview.widget.LinearLayoutManager -> lastVisibleItemPosition = (mLayoutManager as androidx.recyclerview.widget.LinearLayoutManager)
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

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView?)

}
