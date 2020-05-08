package by.nrstudio.mvvm.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment<VB : Any, VM : Any> : Fragment(), KodeinAware {

    companion object {
        const val ARG_OBJECT = "arg_object"
    }

	override val kodein by kodein()

	protected lateinit var binding: ViewDataBinding
	protected lateinit var viewModel: ViewModel

	protected open fun onReloadData() {
		//
	}

	protected open fun clearItems() {
		//
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		initData(inflater)
		return binding.root
	}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

	protected open fun initData(inflater: LayoutInflater) {
		initBinding(inflater)
		binding.lifecycleOwner = this
	}

    protected open fun initListeners() {
        //
    }
	protected fun getBinding() = binding as VB
	protected fun getViewModel() = viewModel as VM

    abstract fun initBinding(inflater: LayoutInflater)
}
