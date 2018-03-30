package org.mainsoft.basewithkodein.screen.fragment.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.theartofdev.edmodo.cropper.CropImage
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.presenter.base.BaseImagePresenter
import java.io.File

abstract class BaseImageCropFragment : BaseFragment() {

    protected lateinit var options: RequestOptions

    companion object {
        private const val ASPECT_Y = 3
        private const val ASPECT_X = 4
    }

    fun initFile(path: File?) {
        CropImage.activity(Uri.fromFile(path)).setAspectRatio(getAspectX(), getAspectY()).start(activity!!)
    }

    protected open fun getAspectX(): Int = ASPECT_X
    protected open fun getAspectY(): Int = ASPECT_Y

    open fun initImage(path: String?) {
        options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.bg_ph_image)
                .error(R.drawable.bg_ph_image)
                .priority(Priority.HIGH)
    }

    fun openAlbums() {
        getPresenter<BaseImagePresenter>()?.openAlbum(this)
    }

    fun openCamera() {
        getPresenter<BaseImagePresenter>()?.openCamera(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                (getPresenter<BaseImagePresenter>())?.setImageBase(File(result.uri.path))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(javaClass.simpleName, result.error.message, result.error)
            }
        }
    }
}
