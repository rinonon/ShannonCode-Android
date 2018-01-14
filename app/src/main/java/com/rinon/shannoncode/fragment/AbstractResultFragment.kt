package com.rinon.shannoncode.fragment

import android.support.v4.app.Fragment

abstract class AbstractResultFragment : Fragment() {
    abstract fun check()
    abstract fun getHintText(): String
}