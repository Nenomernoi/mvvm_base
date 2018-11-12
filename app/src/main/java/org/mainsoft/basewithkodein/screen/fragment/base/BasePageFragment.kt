package org.mainsoft.basewithkodein.screen.fragment.base

import kotlinx.android.synthetic.main.fragment_base_page.*
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.base.BasePageAdapter
import org.mainsoft.basewithkodein.listener.BasePageChangeListener

abstract class BasePageFragment : BaseFragment() {

    protected var pageAdapter: BasePageAdapter? = null

    override fun getLayout(): Int = R.layout.fragment_base_page

    open fun setAdapter(tabsName: MutableList<String>) {
        if (pageAdapter != null) {
            updateAdapter()
            return
        }
        initAdapter()
        pageAdapter!!.setTabs(tabsName)

        initSettingAdapter()
        tlMain.setupWithViewPager(vpMain)
    }

    fun setAdapter() {
        initAdapter()
        initSettingAdapter()
        vpMain.addOnPageChangeListener(object : BasePageChangeListener {
            override fun onPageSelected(position: Int) {
                initPageData(position)
            }
        })
    }

    protected open fun updateAdapter() {
        //
    }

    protected open fun initSettingAdapter() {
        vpMain.adapter = pageAdapter
        setOffscreenPageLimit()
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected open fun initPageData(position: Int) {
        //
    }

    fun openPage(page: Int) {
        //
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected open fun onUpdateChildren() {
        (0 until childFragmentManager.fragments.size)
                .map { childFragmentManager.fragments[it] }
                .forEach { (it as BaseFragment).updateScreen() }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected fun getCurrentFragment(): androidx.fragment.app.Fragment? {

        for (i in 0 until childFragmentManager.fragments.size) {
            val fragment = childFragmentManager.fragments[i]
            if (i == vpMain.currentItem) {
                return fragment
            }
        }
        return null
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected abstract fun initAdapter()
    protected abstract fun setOffscreenPageLimit()
}
