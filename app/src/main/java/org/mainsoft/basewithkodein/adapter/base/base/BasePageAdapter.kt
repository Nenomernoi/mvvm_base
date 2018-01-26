package org.mainsoft.basewithkodein.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

abstract class BasePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var tabs: MutableList<String> = ArrayList()

    fun setTabs(tabs: MutableList<String>) {
        this.tabs = tabs
    }

    override fun getItem(position: Int): Fragment = Fragment()

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
