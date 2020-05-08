package by.nrstudio.mvvm.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener : RecyclerView.OnScrollListener {

    companion object {
        private const val MIN_POSITION_TO_LOAD= 5
    }

	private var visibleThreshold : Int = MIN_POSITION_TO_LOAD
	private var currentPage = 0
	private var previousTotalItemCount = 0
	private var loading = true
	private val startingPageIndex = 0

	private var mLayoutManager: RecyclerView.LayoutManager?

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

	fun clearManager() {
		this.mLayoutManager = null
	}

	private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
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

	override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
		val totalItemCount = mLayoutManager?.itemCount ?: 0

		val lastVisibleItemPosition = when (mLayoutManager) {
			is StaggeredGridLayoutManager -> {
				val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
				getLastVisibleItem(lastVisibleItemPositions)
			}
			is GridLayoutManager -> (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
			is LinearLayoutManager -> (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
			else -> 0
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
			loading = true
			currentPage++
			onLoadMore(currentPage, totalItemCount, view)
		}
	}

	fun resetState() {
		this.currentPage = 0
		this.previousTotalItemCount = 0
		this.loading = true
	}

	fun setCurrentPage(page: Int, previousTotalItemCount: Int) {
		this.currentPage = page
		this.previousTotalItemCount = previousTotalItemCount
		this.loading = true
	}

	abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}
