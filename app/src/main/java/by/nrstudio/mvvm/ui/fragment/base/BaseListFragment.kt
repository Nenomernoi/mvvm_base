package by.nrstudio.mvvm.ui.fragment.base

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nrstudio.mvvm.listener.EndlessScrollListener
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.ui.viewmodel.base.BaseListViewModel

abstract class BaseListFragment<T : Any, VB : Any, VM : BaseListViewModel<T>> : BaseFragment<VB, VM>() {

	protected var adapter: ListAdapter<T, *>? = null

	private val obsData: Observer<MutableList<T>> =
		Observer { its ->
			val start = if (its.size < Repository.LIMIT_PAGE) 0 else its.size - Repository.LIMIT_PAGE
			adapter?.notifyItemRangeChanged(start, its.size)
		}

	protected val endLess: EndlessScrollListener by lazy {
		object : EndlessScrollListener(layoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
				loadNext(page)
			}
		}
	}

	protected open val layoutManager = LinearLayoutManager(activity)

	protected val refListener: SwipeRefreshLayout.OnRefreshListener by lazy {
		SwipeRefreshLayout.OnRefreshListener { onReloadData() }
	}

	override fun initData(inflater: LayoutInflater) {
		initBinding(inflater)
		binding.lifecycleOwner = this
		initAdapter()
	}

	override fun initListeners() {
		getViewModel().items.observeForever(obsData)
	}

	override fun onReloadData() {
		clearItems()
		loadNext(0)
	}

	protected open fun loadNext(page: Int) {
		getViewModel().onLoadNext(page)
	}

	override fun clearItems() {
		endLess.resetState()
		getViewModel().clearData()
	}

	override fun onDestroyView() {
		getViewModel().items.observeForever(obsData)
		super.onDestroyView()
	}

	abstract fun initAdapter()
}
