package org.mainsoft.basewithkodein.screen.fragment

import android.Manifest
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_base.btnImage
import kotlinx.android.synthetic.main.fragment_base.btnPermission
import kotlinx.android.synthetic.main.fragment_base.btnPhoto
import kotlinx.android.synthetic.main.fragment_base.imgBg
import kotlinx.android.synthetic.main.fragment_base.txtList
import org.kodein.direct
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.activity.base.PermissionListener
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.MainPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.MainView

class MainFragment : BaseFragment(), MainView {

    init {
        presenter = App.kodein.direct.instance<MainView, MainPresenter>(arg = this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): MainFragment {

            val pageFragment = MainFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    override fun initListeners() {
        txtList.setOnClickListener({ onClickDrawer() })
        btnPhoto.setOnClickListener({ onClickPhoto() })
        btnImage.setOnClickListener({ onClickImage() })
        btnPermission.setOnClickListener({ onClickPermission() })
    }

    private fun onClickDrawer() {
        activityCallback.openFragment(MainListFragment::class.java, true, Bundle())
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    override fun getLayout(): Int = R.layout.fragment_base

    ///////////////////////////////////////////////////////////////////////////////////////

    override fun initImage(path: String) {
        Glide.with(activity).load(path).into(imgBg)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private fun onClickPhoto() {
        getPresenter<MainPresenter>()?.openCamera(this)
    }

    private fun onClickImage() {
        getPresenter<MainPresenter>()?.openAlbum(this)
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private fun onClickPermission() {

        activityCallback.openPermission(object : PermissionListener {
            override fun onComplete() {
                activityCallback.initLocation()
            }

            override fun onError() {
                showHideProgress(false)
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)

    }

}