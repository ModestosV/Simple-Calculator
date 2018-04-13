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

        var view = inflater.inflate(R.layout.activity_binary_calculator, container, false)
        val page = getArguments()!!.getInt(PAGE_NUM)

        if(page == 0)
            view = inflater.inflate(R.layout.activity_binary_calculator, container, false)
        else if(page == 1)
            view = inflater.inflate(R.layout.activity_octodecimal_calculator, container, false)
        else if(page == 2)
            view = inflater.inflate(R.layout.activity_hexadecimal_calculator, container, false)

        return view
    }

    companion object {
        val PAGE_NUM = "PAGE_NUM"
        fun newInstance(page: Int): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putInt(PAGE_NUM, page)
            fragment.setArguments(args)
            return fragment
        }
    }
}