package com.simplemobiletools.calculator.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.simplemobiletools.calculator.R
import com.simplemobiletools.calculator.helpers.PageAdapter
import com.simplemobiletools.calculator.helpers.PageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.binary_wrapper.*


class BinaryCalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.binary_wrapper)

        val pageAdapter = PageAdapter(supportFragmentManager)


        pageAdapter.add(PageFragment.newInstance(0), "Binary")
        pageAdapter.add(PageFragment.newInstance(1), "Octal")
        pageAdapter.add(PageFragment.newInstance(2), "Hexa")


        view_pager.adapter = pageAdapter
        tabs.setupWithViewPager(view_pager)
    }

}

