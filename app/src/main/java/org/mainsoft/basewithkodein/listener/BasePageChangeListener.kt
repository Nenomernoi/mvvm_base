package org.mainsoft.basewithkodein.listener

interface BasePageChangeListener : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
        //
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
    }

    override fun onPageSelected(position: Int) {
        //
    }
}
