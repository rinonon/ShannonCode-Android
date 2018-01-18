package com.rinon.shannoncode.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R

class DescriptionShannonFanoFragment : Fragment() {

    companion object {
        fun newInstance(): DescriptionShannonFanoFragment {
            return DescriptionShannonFanoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_description_shannon_fano, container, false)
    }
}
