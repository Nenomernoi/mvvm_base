package org.mainsoft.basewithkodein.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import org.mainsoft.basewithkodein.base.BasePageAdapter
import org.mainsoft.basewithkodein.screen.fragment.MainFragment
import org.mainsoft.basewithkodein.screen.fragment.MainListFragment

class MainPageAdapter(fm: FragmentManager) : BasePageAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> MainListFragment.newInstance(position)
            0 -> MainFragment.newInstance(position)
            else -> MainFragment.newInstance(position)
        }

    }
}