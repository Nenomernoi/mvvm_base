package by.nrstudio.mvvm.ui.fragment.breeds

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.nrstudio.mvvm.adapter.BreedAdapter
import by.nrstudio.mvvm.adapter.BreedListener
import by.nrstudio.mvvm.databinding.FragmentBreedListRefreshBinding
import by.nrstudio.mvvm.net.Repository
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.fragment.base.BaseListFragment
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModel
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModelFactory
import org.kodein.di.generic.instance

class BreedsFragment : BaseListFragment<Breed, FragmentBreedListRefreshBinding, BreedsViewModel>() {

	private val factory by instance<BreedsViewModelFactory>()

	private val obsData: Observer<MutableList<Breed>> = Observer { its ->
		val start = if (its.size < Repository.LIMIT_PAGE) 0 else its.size - Repository.LIMIT_PAGE
		adapter?.notifyItemRangeChanged(start, its.size)
	}

	override fun initAdapter() {
		adapter = BreedAdapter(listener = BreedListener {
			//
		})
		getBinding().rvMain.adapter = adapter
	}

	override fun initBinding(inflater: LayoutInflater) {
		binding = FragmentBreedListRefreshBinding.inflate(inflater)
		viewModel = ViewModelProvider(this, factory).get(BreedsViewModel::class.java)
		getBinding().breedsViewModel = getViewModel()
		getBinding().rvMain.layoutManager = layoutManager
	}

	override fun initListeners() {
		getBinding().rvMain.addOnScrollListener(endLess)
		getBinding().sRefresh.setOnRefreshListener(refListener)
		getViewModel().breeds.observeForever(obsData)
	}

	override fun onDestroyView() {
		getViewModel().breeds.observeForever(obsData)
		super.onDestroyView()
	}

	override fun loadNext(page: Int) {
		getViewModel().onLoadNext(page)
	}

	override fun clearItems() {
		super.clearItems()
		getViewModel().clearData()
	}
}
