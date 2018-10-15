package org.mainsoft.basewithkodein.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

abstract class BasePageAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private var tabs: MutableList<String> = ArrayList()

    fun setTabs(tabs: MutableList<String>) {
        this.tabs = tabs
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment = androidx.fragment.app.Fragment()

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence = tabs[position]
}
