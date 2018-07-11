package org.mainsoft.basewithkodein.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import org.mainsoft.basewithkodein.base.BasePageAdapter
import org.mainsoft.basewithkodein.screen.fragment.ExampleFragment
import org.mainsoft.basewithkodein.screen.fragment.ExampleListFragment

class ExamplePageAdapter(fm: FragmentManager) : BasePageAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> ExampleListFragment.newInstance(position)
            0 -> ExampleFragment.newInstance(position)
            else -> ExampleFragment.newInstance(position)
        }

    }
}
