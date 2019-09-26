package org.mainsoft.base.screen.fragment

import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_images.*
import org.mainsoft.base.R
import org.mainsoft.base.lib.ViewStateStore
import org.mainsoft.base.net.response.Breed
import org.mainsoft.base.net.response.Image
import org.mainsoft.base.screen.fragment.base.BaseFragment
import org.mainsoft.base.screen.model.base.BaseViewModel
import org.mainsoft.base.screen.model.images.ImagesViewModel
import org.mainsoft.base.screen.model.images.ImagesViewModelFactory
import org.mainsoft.base.screen.model.images.ImagesViewState
import org.mainsoft.base.util.ImageSignature

class ImagesFragment : BaseFragment() {

    override fun getLayout(): Int = R.layout.fragment_images

    override fun initData() {
        viewModel = ViewModelProviders.of(this, ImagesViewModelFactory).get()
        val breed = arguments?.getParcelable(BaseViewModel.ARGUMENT_EXTRA) as? Breed
        getViewModel<ImagesViewModel>().id = breed?.id
        txtTitle?.text = breed?.name
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
                        getViewModel<ImagesViewModel>().swipe(currentId == R.id.offScreenLike)
                    }
                }
            }
        })
        getViewModel<ImagesViewModel>()
                .getStore<ViewStateStore<ImagesViewState>>()
                .observe(this) {
                    if (!it.loading) {
                        setData(it.data)
                    }
                    showHideProgress(it.loading)

                    showHideNoData(it.error != null)
                    showMessageError(it.error?.message)
                }
    }

    private fun setData(items: MutableList<Image>) {
        if (items.isNullOrEmpty()) {
            return
        }
        val currentIndex = items.size - 1

        loadImage(imgTop, items[currentIndex])

        if (items.size == 1) {
            loadImage(imgBottom, items[currentIndex])
            getViewModel<ImagesViewModel>().loadNext()
            return
        }
        loadImage(imgBottom, items[currentIndex - 1])
    }


    private fun loadImage(img: ImageView, it: Image) {

        val req = RequestOptions()
                .signature(ImageSignature(it.id))
                .placeholder(R.drawable.bg_cat)
                .error(R.drawable.bg_cat)

        Glide.with(activity ?: return)
                .load(it.url)
                .apply(req)
                .into(img)
    }

}