package org.mainsoft.base.screen.fragment

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_images.*
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

        mlMain?.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.offScreenPass,
                    R.id.offScreenLike -> {
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.rest, R.id.like)
                        getViewModel<ImagesViewModel>().swipe()
                    }
                }
            }

        })
/*
        btnLoadMore?.setOnClickListener {
            getViewModel<ImagesViewModel>().loadNext()
        }
*/
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

        if (items.isNullOrEmpty()) {
            return
        }
        val currentIndex = items.size - 1

        Glide.with(activity ?: return)
                .load(items[currentIndex].url)
                .placeholder(R.drawable.bg_cat)
                .error(R.drawable.bg_cat)
                .into(imgTop)

        val prevIndex = if (currentIndex - 1 > 0) currentIndex - 1 else 0

        Glide.with(activity ?: return)
                .load(items[prevIndex].url)
                .placeholder(R.drawable.bg_cat)
                .error(R.drawable.bg_cat)
                .into(imgBottom)
    }
}