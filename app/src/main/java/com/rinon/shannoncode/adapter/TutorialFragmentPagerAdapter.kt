package com.rinon.shannoncode.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rinon.shannoncode.fragment.tutorial.*

class TutorialFragmentPagerAdapter(fragmentManager: FragmentManager, private val PAGE_NUM: Int) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> Tutorial1Fragment.newInstance()
            1 -> Tutorial2Fragment.newInstance()
            2 -> Tutorial3Fragment.newInstance()
            3 -> Tutorial4Fragment.newInstance()
            4 -> Tutorial5Fragment.newInstance()
            else -> throw Exception("fragment error")
        }
    }

    override fun getCount(): Int {
        return PAGE_NUM
    }
}
