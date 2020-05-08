package by.nrstudio.mvvm.ui.fragment.breeds

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import by.nrstudio.mvvm.databinding.FragmentBreedBinding
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.fragment.base.BaseFragment
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedViewModel
import by.nrstudio.mvvm.ui.viewmodel.breeds.BreedViewModelFactory
import kotlinx.android.synthetic.main.fragment_breed.*
import org.kodein.di.generic.instance

class BreedFragment : BaseFragment<FragmentBreedBinding, BreedViewModel>() {

    private val factory by instance<BreedViewModelFactory>()

    override fun initBinding(inflater: LayoutInflater) {
        binding = FragmentBreedBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, factory).get(BreedViewModel::class.java)
        getBinding().breedViewModel = getViewModel()
    }

    override fun initData(inflater: LayoutInflater) {
        super.initData(inflater)
        getViewModel().loadData(arguments?.getParcelable(ARG_OBJECT) as? Breed)
    }
}
