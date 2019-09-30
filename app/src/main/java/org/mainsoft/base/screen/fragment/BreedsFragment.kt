package org.mainsoft.base.screen.fragment

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_breeds.*
import org.mainsoft.base.R
import org.mainsoft.base.adapter.BreedListAdapter
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.listeners.BackCallback
import org.mainsoft.base.listeners.BreedsReturnCallback
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.screen.fragment.base.BaseSwipeEndlessListFragment
import org.mainsoft.base.screen.model.breeds.BreedsViewModel
import org.mainsoft.base.screen.model.breeds.BreedsViewModelFactory
import org.mainsoft.base.screen.model.breeds.BreedsViewState

class BreedsFragment : BaseSwipeEndlessListFragment<Breed>() {

    private val heightBar by lazy {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    override fun getLayout(): Int = R.layout.fragment_breeds

    private val backListener = BackCallback(object : BreedsReturnCallback {
        override fun onUpdateItem(position: Int) {
            getViewModel<BreedsViewModel>().updateItem(position)
        }
    })

    override fun initData() {
        viewModel = ViewModelProviders.of(this, BreedsViewModelFactory).get()
        super.initData()
    }

    override fun initListeners() {
        super.initListeners()

        mlMain?.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.click -> {
                     //   motionLayout.progress = 0f
                     //   motionLayout.setTransition(R.id.click, R.id.rest)
                     //   motionLayout.transitionToEnd()
                    }
                }
            }
        })

        getViewModel<BreedsViewModel>()
                .getStore<ViewStateStore<BreedsViewState>>()
                .observe(this) {

                    showHideProgress(it.loading)
                    showHideRefresh(it.refresh)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)

                    if (it.model != null) {
                        initBreedOpen(it.model, it.originalPos)
                        return@observe
                    } else {
                        if (it.data.isNotEmpty()) {
                            initBreedOpen(it.data[0])
                        }
                    }

                    setData(it.data, it.page)
                }
    }

    override fun initAdapter() {
        adapter = BreedListAdapter(getViewModel())
    }

    override fun loadData() {
        getViewModel<BreedsViewModel>().loadData()
    }

    override fun loadNext(page: Int) {
        getViewModel<BreedsViewModel>().loadNext(page)
    }

    override fun onReloadData() {
        clearItems()
        getViewModel<BreedsViewModel>().reloadData()
    }

    //////////////////////////////////////////////////////

    private fun openBreed(position: Int, model: Breed) {
/*
        navigate(R.id.action_breedsFragment_to_breedFragment,
                bundleOf(BaseViewModel.ARGUMENT_ID to position,
                        BaseViewModel.ARGUMENT_EXTRA to model,
                        BaseViewModel.ARGUMENT_RETURN to backListener))

 */
    }

    private fun initBreedOpen(model: Breed, originalPos: IntArray? = null) {

        txtTitle?.text = model.name
        txtDescription?.text = model.description

        btnFavorite?.setImageResource(if (model.favorite) R.drawable.ic_favorite_color else R.drawable.ic_favorite_border_color)

        Glide.with(activity ?: return)
                .load(model.image_url)
                .centerCrop()
                .placeholder(R.drawable.ic_cat)
                .error(R.drawable.ic_cat)
                .apply(RequestOptions.circleCropTransform())
                .into(imgMain ?: return)

        originalPos?.apply {
            mlMain?.getConstraintSet(R.id.start)?.let {
                it.setMargin(R.id.rlImage, ConstraintSet.TOP, originalPos[1] - heightBar)
            }
            mlMain?.setTransition(R.id.rest, R.id.start)
            mlMain?.transitionToEnd()
        }
/*
        mlMain?.transitionToState(R.id.click)
 */
    }

}
