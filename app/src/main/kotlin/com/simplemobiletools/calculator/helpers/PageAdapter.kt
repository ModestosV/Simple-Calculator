package com.simplemobiletools.calculator.helpers
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val nameOfTabs: ArrayList<String>
    private val partialPage: ArrayList<Fragment>

    init {
        nameOfTabs = ArrayList()
        partialPage = ArrayList()
    }

    fun add(fragment: Fragment, title: String) {
        nameOfTabs.add(title)
        partialPage.add(fragment)
    }

    override fun getCount(): Int {
        return partialPage.size
    }

    override fun getItem(position: Int): Fragment {
        return partialPage[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return nameOfTabs[position]
    }
}