package by.nrstudio.mvvm.ui.fragment.breeds

import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.nrstudio.mvvm.adapter.BreedAdapter
import by.nrstudio.mvvm.adapter.BreedListener
import by.nrstudio.mvvm.databinding.FragmentBreedListRefreshBinding
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.fragment.base.BaseListFragment
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModel
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModelFactory
import org.kodein.di.generic.instance

class BreedsFragment : BaseListFragment<Breed, FragmentBreedListRefreshBinding, BreedsViewModel>() {

	private val factory by instance<BreedsViewModelFactory>()

	override fun initAdapter() {
		adapter = BreedAdapter(listener = BreedListener {
			//
			Toast.makeText(activity, "You clicked on ${it.name}", Toast.LENGTH_SHORT).show()
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
		super.initListeners()
	}
}
