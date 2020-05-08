package by.nrstudio.mvvm.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nrstudio.mvvm.R
import by.nrstudio.mvvm.adapter.BreedAdapter
import by.nrstudio.mvvm.net.ApiRest
import by.nrstudio.mvvm.net.response.Breed
import by.nrstudio.mvvm.ui.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

// ///////////////////////// Base /////////////////////// //

@BindingAdapter("pbStatus")
fun pbStatus(pb: View?, status: Status?) {
	when (status) {
		Status.LOADING -> {
			pb?.isVisible = true
		}
		Status.DONE, Status.ERROR -> {
			pb?.isVisible = false
		}
		else ->
			pb?.isVisible = true
	}
}

@BindingAdapter("refStatus")
fun refStatus(srl: SwipeRefreshLayout?, status: Status?) {
	when (status) {
		Status.LOADING, Status.DONE, Status.ERROR -> {
			srl?.isRefreshing = false
		}
		else ->
			srl?.isRefreshing = true
	}
}

// ///////////////////////// Breeds screen /////////////////////// //

@BindingAdapter("listBreeds")
fun bindRecyclerView(recyclerView: RecyclerView?, data: List<Breed>?) {
	val adapter = recyclerView?.adapter as BreedAdapter
	data?.let {
		adapter.submitList(data)
	}
}

@BindingAdapter("textInt")
fun setTextInt(view: TextView?, value: Int?) {
    view?.text = "${value?.toString()}"
}

@BindingAdapter("imageBreed", "circle")
fun imageBreed(view: ImageView?, model: Breed?, isCircle: Boolean) {

	val req = RequestOptions()
		.signature(ImageSignature("${model?.id} ${model?.image_url}"))
		.timeout(ApiRest.IMAGE_TIME_OUT)
        .placeholder(if (isCircle) R.drawable.bg_image else R.drawable.bg_shadow)
		.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
		.error(R.drawable.ic_cat)

	if (isCircle) {
		req.circleCrop()
	}

	view?.let {
		Glide.with(view.context)
			.load(model?.image_url)
			.apply(req)
            .transition(DrawableTransitionOptions.withCrossFade(ApiRest.FADE_TIME_OUT))
			.into(view)
	}
}

// ///////////////////////// Splash screen /////////////////////// //

@BindingAdapter("animSplash")
fun animSplash(view: View?, status: Status?) {
	when (status) {
		Status.LOADING -> {
			view?.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.splash_anim))
		}
		Status.ERROR -> {
			view?.clearAnimation()
		}
		else ->
			view?.clearAnimation()
	}
}
