package by.nrstudio.mvvm.ui.fragment.base

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nrstudio.mvvm.listener.EndlessScrollListener

abstract class BaseListFragment<T : Any, VB : Any, VM : Any> : BaseFragment<VB, VM>() {

	protected var adapter: ListAdapter<T, *>? = null

	protected val endLess: EndlessScrollListener by lazy {
		object : EndlessScrollListener(layoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
				loadNext(page)
			}
		}
	}

	protected val refListener: SwipeRefreshLayout.OnRefreshListener by lazy {
		SwipeRefreshLayout.OnRefreshListener { onReloadData() }
	}

	override fun onReloadData() {
		clearItems()
		loadNext(0)
	}

	protected open val layoutManager = LinearLayoutManager(activity)

	protected open fun loadNext(page: Int) {
		//
	}

	override fun clearItems() {
		endLess.resetState()
	}

	override fun initData(inflater: LayoutInflater) {
		initBinding(inflater)
		binding.lifecycleOwner = this
		initAdapter()
	}

	abstract fun initAdapter()
}
