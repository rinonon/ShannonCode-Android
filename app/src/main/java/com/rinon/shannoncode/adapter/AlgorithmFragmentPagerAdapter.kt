package com.rinon.shannoncode.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rinon.shannoncode.fragment.DescriptionShannonFanoFragment
import com.rinon.shannoncode.fragment.DescriptionShannonFragment

class AlgorithmFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val pageTitle = listOf("Shannon Coding", "Shannon-Fano Coding")

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> DescriptionShannonFragment.newInstance()
            1 -> DescriptionShannonFanoFragment.newInstance()
            else -> throw Exception("fragment error")
        }
    }

    override fun getCount(): Int {
        return pageTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return pageTitle[position]
    }
}
