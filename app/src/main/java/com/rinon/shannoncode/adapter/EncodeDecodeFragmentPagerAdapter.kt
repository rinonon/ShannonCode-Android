package com.rinon.shannoncode.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rinon.shannoncode.fragment.DecodeFragment
import com.rinon.shannoncode.fragment.EncodeFragment
import com.rinon.shannoncode.model.AbstractCode

class EncodeDecodeFragmentPagerAdapter(fragmentManager: FragmentManager,
                                       private val codeList: ArrayList<AbstractCode>) : FragmentPagerAdapter(fragmentManager) {

    private val pageTitle = listOf("Encode", "Decode")

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> EncodeFragment.newInstance(codeList)
            1 -> DecodeFragment.newInstance(codeList)
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