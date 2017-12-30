package com.rinon.shannoncode.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rinon.shannoncode.fragment.DecodeFragment
import com.rinon.shannoncode.fragment.EncodeFragment
import com.rinon.shannoncode.model.AbstractContent

class EncodeDecodeFragmentPagerAdapter(fragmentManager: FragmentManager,
                                       private val contentList: ArrayList<AbstractContent>) : FragmentPagerAdapter(fragmentManager) {

    private val pageTitle = listOf("Encode", "Decode")

    override fun getItem(position: Int): Fragment {
       when(position) {
           0 -> return EncodeFragment.newInstance(contentList)
           1 -> return DecodeFragment.newInstance(contentList)
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