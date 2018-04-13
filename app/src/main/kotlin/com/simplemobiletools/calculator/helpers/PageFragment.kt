package com.simplemobiletools.calculator.helpers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.simplemobiletools.calculator.R

class PageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_binary_calculator, container, false)
        val page = getArguments()!!.getInt(PAGE_NUM)
        return view
    }

    companion object {
        val PAGE_NUM = "PAGE_NUM"
        fun newInstance(page: Int): PageFragment {
            val partialPage = PageFragment()
            val args = Bundle()
            args.putInt(PAGE_NUM, page)
            partialPage.setArguments(args)
            return partialPage
        }
    }
}