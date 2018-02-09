package com.rinon.shannoncode.fragment.tutorial

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R

class Tutorial5Fragment : Fragment() {

    companion object {
        fun newInstance(): Tutorial5Fragment {
            return Tutorial5Fragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tutorial_5, container, false)
    }
}
