package org.mainsoft.base.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_menu.*
import org.mainsoft.base.R
import org.mainsoft.base.screen.fragment.base.BaseItemFragment
import java.util.*

class MenuActivity : AppCompatActivity(),
        ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    // overall back stack of containers
    private val backStack = Stack<Int>()

    // list of base destination containers
    private val fragments = arrayListOf(
            BaseItemFragment.newInstance(R.layout.content_home_base, R.id.nav_host_home),
            BaseItemFragment.newInstance(R.layout.content_grid_base, R.id.nav_host_grid))

    // map of navigation_id to container index
    private val indexToPage = mapOf(0 to R.id.home, 1 to R.id.grid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // setup main view pager
        main_pager.addOnPageChangeListener(this)
        main_pager.adapter = ViewPagerAdapter()
        main_pager.post(this::checkDeepLink)
        main_pager.offscreenPageLimit = fragments.size

        // set bottom nav
        bottom_nav.setOnNavigationItemSelectedListener(this)
        bottom_nav.setOnNavigationItemReselectedListener(this)

        // initialize backStack with elements
        if (backStack.empty()) backStack.push(0)
    }

    /// BottomNavigationView ItemSelected Implementation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val position = indexToPage.values.indexOf(item.itemId)
        if (main_pager.currentItem != position) setItem(position)
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        val position = indexToPage.values.indexOf(item.itemId)
        val fragment = fragments[position]
        fragment.popToRoot()
    }

    override fun onBackPressed() {
        val fragment = fragments[main_pager.currentItem]
        val hadNestedFragments = fragment.onBackPressed()
        // if no fragments were popped

        Log.e("Navigation", "first-> ${fragments[0].childFragmentManager.fragments[0].childFragmentManager.fragments.size} " +
                "second-> ${fragments[1].childFragmentManager.fragments[0].childFragmentManager.fragments.size}")

        if (!hadNestedFragments) {
            if (backStack.size > 1) {
                // remove current position from stack
                backStack.pop()
                // set the next item in stack as current
                main_pager.currentItem = backStack.peek()

            } else super.onBackPressed()
        }
    }

    /// OnPageSelected Listener Implementation
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(page: Int) {
        val itemId = indexToPage[page] ?: R.id.home
        if (bottom_nav.selectedItemId != itemId) bottom_nav.selectedItemId = itemId
    }

    private fun setItem(position: Int) {
        main_pager.currentItem = position
        backStack.push(position)
    }

    private fun checkDeepLink() {
        fragments.forEachIndexed { index, fragment ->
            val hasDeepLink = fragment.handleDeepLink(intent)
            if (hasDeepLink) setItem(index)
        }
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getCount(): Int = fragments.size
    }
}