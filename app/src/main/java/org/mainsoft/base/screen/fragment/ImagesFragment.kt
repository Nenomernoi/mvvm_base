package org.mainsoft.base.screen.fragment

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.images.ImagesViewModel
import org.mainsoft.base.screen.model.images.ImagesViewModelFactory
import org.mainsoft.base.screen.model.images.ImagesViewState

class ImagesFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_images

    override fun initData() {
        viewModel = ViewModelProviders.of(this, ImagesViewModelFactory).get()
        getViewModel<ImagesViewModel>().id = arguments?.getString(BaseViewModel.ARGUMENT_ID) ?: return
    }

    override fun initListeners() {
        super.initListeners()
        getViewModel<ImagesViewModel>()
                .getStore<ViewStateStore<ImagesViewState>>()
                .observe(this) {

                    showHideProgress(it.loading)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)
                    setData(it.data)
                }
    }

    private fun setData(items: MutableList<Image>) {

    }
}