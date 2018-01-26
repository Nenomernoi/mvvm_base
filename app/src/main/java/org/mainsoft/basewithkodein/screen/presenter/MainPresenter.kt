package org.mainsoft.basewithkodein.screen.presenter

import android.content.Context
import android.os.Bundle
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.MainView
import java.io.File

class MainPresenter(view: MainView) : BasePresenter(view) {

    private lateinit var image: File

    override fun initData(context: Context, bundle: Bundle?, arguments: Bundle?) {
        //
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    fun openCamera(fr: BaseFragment) {
        RxPaparazzo.single(fr)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<MainView>()?.initImage(image.absolutePath)
                    }
                })
    }

    fun openAlbum(fr: BaseFragment) {
        RxPaparazzo.single(fr)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<MainView>()?.initImage(image.absolutePath)
                    }
                })
    }

}