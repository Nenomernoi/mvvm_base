package by.nrstudio.mvvm.ui.fragment.breeds

import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import by.nrstudio.mvvm.R
import by.nrstudio.mvvm.adapter.BreedAdapter
import by.nrstudio.mvvm.adapter.BreedListener
import by.nrstudio.mvvm.databinding.FragmentBreedListRefreshBinding
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.fragment.base.BaseListFragment
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModel
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedsViewModelFactory
import by.nrstudio.mvvm.util.navigate
import org.kodein.di.generic.instance

class BreedsFragment : BaseListFragment<Breed, FragmentBreedListRefreshBinding, BreedsViewModel>() {

	private val factory by instance<BreedsViewModelFactory>()

	override fun initAdapter() {
		adapter = BreedAdapter(listener = BreedListener {
            navigate(
                R.id.action_breedsFragment_to_breedFragment,
                bundleOf(ARG_OBJECT to it)
            )
		})
		getBinding().rvMain.adapter = adapter
	}

	override fun initBinding(inflater: LayoutInflater) {
		binding = FragmentBreedListRefreshBinding.inflate(inflater)
		viewModel = ViewModelProvider(this, factory).get(BreedsViewModel::class.java)
		getBinding().breedsViewModel = getViewModel()
        getBinding().rvMain.apply {
            if (this.layoutManager == null) {
                this.layoutManager = lManager
            }
        }
	}

	override fun initListeners() {
		getBinding().rvMain.addOnScrollListener(endLess)
		getBinding().sRefresh.setOnRefreshListener(refListener)
		super.initListeners()
	}
}
