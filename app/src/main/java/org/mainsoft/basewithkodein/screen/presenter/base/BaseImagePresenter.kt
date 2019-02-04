package org.mainsoft.basewithkodein.screen.presenter.base

import android.content.Context
import android.content.DialogInterface
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.view.base.BaseImageView
import org.mainsoft.basewithkodein.util.DialogUtil
import java.io.File

abstract class BaseImagePresenter(view: BaseImageView) : BasePresenter(view) {

    protected var image: File? = null

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    fun openImageDialog(context: Context) {

        val list = mutableListOf(context.resources.getString(R.string.popup_open_image),
                context.resources.getString(R.string.popup_open_photo), context.resources.getString(R.string.cancel))

        DialogUtil.showImageDialog(context, list, object : DialogUtil.Companion.ListenerDialogList {
            override fun onSelectItem(position: Int, dialog: DialogInterface) {
                when (position) {
                    0 -> {
                        showHideProgress(false)
                        getView<BaseImageView>()?.openAlbums()
                    }
                    1 -> {
                        showHideProgress(false)
                        getView<BaseImageView>()?.openCamera()
                    }
                    2 -> {

                    }
                }
            }
        })
    }

    fun openCamera(fr: BaseFragment) {
        addSubscription(RxPaparazzo.single(fr)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<BaseImageView>()?.initFile(image)
                    }
                })
    }

    fun openAlbum(fr: BaseFragment) {
        addSubscription(RxPaparazzo.single(fr)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<BaseImageView>()?.initFile(image)
                    }
                })
    }

    fun setImageBase(img: File?) {
        image?.delete()
        this.image = img
        getView<BaseImageView>()?.initImage(image!!.path)
    }
}
