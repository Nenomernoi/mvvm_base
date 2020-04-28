package by.nrstudio.mvvm.ui.fragment.spash

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.nrstudio.mvvm.R
import by.nrstudio.mvvm.databinding.FragmentSplashBinding
import by.nrstudio.mvvm.ui.Status
import by.nrstudio.mvvm.ui.fragment.base.BaseFragment
import by.nrstudio.mvvm.ui.viewmodel.splash.SplashViewModel
import by.nrstudio.mvvm.ui.viewmodel.splash.SplashViewModelFactory
import by.nrstudio.mvvm.util.navigateSplash
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(), KodeinAware {

	private val factory by instance<SplashViewModelFactory>()

	private val obsNextPage: Observer<Status> = Observer {
		if (it == Status.DONE) {
			navigateSplash(R.id.action_splashFragment_to_breedsFragment)
		}
	}

	override fun initBinding(inflater: LayoutInflater) {
		binding = FragmentSplashBinding.inflate(inflater)
		viewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
		getBinding().splashModel = getViewModel()
	}

	override fun initListeners() {
		getViewModel().status.observeForever(obsNextPage)
	}

	override fun onDestroyView() {
		getViewModel().status.observeForever(obsNextPage)
		super.onDestroyView()
	}
}
