package org.mainsoft.basewithkodein.adapter.base

import androidx.fragment.app.FragmentManager

abstract class BasePageAdapter(fm: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private var tabs: MutableList<String> = ArrayList()

    fun setTabs(tabs: MutableList<String>) {
        this.tabs = tabs
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment = androidx.fragment.app.Fragment()

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
