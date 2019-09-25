package org.mainsoft.base.screen.fragment

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_breed.*
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.fragment.base.ResultCallback
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.breed.BreedViewModel
import org.mainsoft.base.screen.model.breed.BreedViewModelFactory
import org.mainsoft.base.screen.model.breed.BreedViewState
import org.mainsoft.base.util.navigate
import org.mainsoft.base.util.onBack

class BreedFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_breed

    override fun initData() {
        viewModel = ViewModelProviders.of(this, BreedViewModelFactory).get()
        val breed = arguments?.getParcelable(BaseViewModel.ARGUMENT_EXTRA) as? Breed
        getViewModel<BreedViewModel>().setModel(breed)

        if (breed != null) {
            showHideProgress(false)
            setData(breed)
            return
        }
        getViewModel<BreedViewModel>().loadData()
    }

    private fun sendUpdateEvent() {
        val position = arguments?.getInt(BaseViewModel.ARGUMENT_ID) ?: return
        val callback = arguments?.getSerializable(BaseViewModel.ARGUMENT_RETURN) as? BreedsReturnCallback?
        callback?.onUpdateItem(position)
    }

    override fun initListeners() {
        super.initListeners()

        btnBack?.setOnClickListener {
            onBack()
        }
        imgMain?.setOnClickListener {
            val breed = arguments?.getParcelable(BaseViewModel.ARGUMENT_EXTRA) as? Breed
            navigate(R.id.action_breedFragment_to_imagesFragment, bundleOf(BaseViewModel.ARGUMENT_ID to breed?.id))
        }
        fbAdd?.setOnClickListener {
            getViewModel<BreedViewModel>().addToFavorite()
        }
        getViewModel<BreedViewModel>()
                .getStore<ViewStateStore<BreedViewState>>()
                .observe(this) {

                    if (it.update) {
                        sendUpdateEvent()
                        return@observe
                    }

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

        txtAltName?.text = model.alt_names
        txtAltName?.isVisible = !model.alt_names.isNullOrEmpty()

        txtTemperament?.text = model.temperament
        txtOrigin?.text = model.origin
        txtLifeSpan?.text = model.life_span

        txtLink?.text = model.getLinks()

        txtAdaptability?.text = model.adaptability.toString()
        txtAdaptabilityLevel?.text = model.affection_level.toString()
        txtChildFriendly?.text = model.child_friendly.toString()
        txtDogFriendly?.text = model.dog_friendly.toString()

        txtEnergyLevel?.text = model.energy_level.toString()
        txtGrooming?.text = model.grooming.toString()
        txtHealthIssues?.text = model.health_issues.toString()
        txtIntelligence?.text = model.intelligence.toString()

        txtSheddingLevel?.text = model.shedding_level.toString()
        txtSocialNeeds?.text = model.social_needs.toString()
        txtStrangerFriendly?.text = model.stranger_friendly.toString()
        txtVocalisation?.text = model.vocalisation.toString()

        fbAdd?.setImageResource(if (model.favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)

        Glide.with(activity ?: return)
                .load(model.image_url)
                .centerCrop()
                .placeholder(R.drawable.bg_cat)
                .error(R.drawable.bg_cat)
                .into(imgMain)
    }

}

interface BreedsReturnCallback : ResultCallback {
    fun onUpdateItem(position: Int)
}
