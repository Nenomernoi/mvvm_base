package org.mainsoft.base.screen.fragment

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_breed.*
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.model.breed.BreedViewModel
import org.mainsoft.base.screen.model.breed.BreedViewModelFactory
import org.mainsoft.base.screen.model.breed.BreedViewState

class BreedFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_breed

    override fun initData() {
        viewModel = ViewModelProviders.of(this, BreedViewModelFactory).get()
        getViewModel<BreedViewModel>().id = "pers"
    }


    override fun initListeners() {
        super.initListeners()

        getViewModel<BreedViewModel>()
                .getStore<ViewStateStore<BreedViewState>>()
                .observe(this) {

                    showHideProgress(it.loading)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)

                    setData(it.data)
                }
    }

    private fun setData(model: Breed?) {
        model ?: return

        txtTitle?.text = model.name
        txtDescription?.text = model.description

        Glide.with(activity ?: return)
                .load(model.image_url)
                .centerCrop()
                .placeholder(R.drawable.ic_cat)
                .error(R.drawable.ic_cat)
                .apply(RequestOptions.circleCropTransform())
                .into(imgMain)
    }

}
