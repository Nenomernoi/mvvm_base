package org.mainsoft.basewithkodein.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.mainsoft.basewithkodein.base.BasePageAdapter
import org.mainsoft.basewithkodein.screen.fragment.ExampleFragment
import org.mainsoft.basewithkodein.screen.fragment.ExampleListFragment

class ExamplePageAdapter(fm: androidx.fragment.app.FragmentManager) : BasePageAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            1 -> ExampleListFragment.newInstance(position)
            0 -> ExampleFragment.newInstance(position)
            else -> ExampleFragment.newInstance(position)
        }

    }
}
