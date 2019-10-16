package org.mainsoft.basewithkodein.adapter

import androidx.fragment.app.FragmentManager
import org.mainsoft.basewithkodein.adapter.base.BasePageAdapter
import org.mainsoft.basewithkodein.screen.fragment.ExampleFragment
import org.mainsoft.basewithkodein.screen.fragment.ExampleGridFragment
import org.mainsoft.basewithkodein.screen.fragment.ExampleListFragment

class ExamplePageAdapter(fm: FragmentManager) : BasePageAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            1 -> ExampleListFragment.newInstance(position)
            0 -> ExampleFragment.newInstance(position)
            else -> ExampleGridFragment.newInstance(position)
        }

    }
}
