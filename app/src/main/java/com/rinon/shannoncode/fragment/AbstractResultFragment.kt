package com.rinon.shannoncode.fragment

import android.support.v4.app.Fragment

abstract class AbstractResultFragment : Fragment() {

    object Grid {
        var x: Int = 0
        var y: Int = 0
    }

    enum class Status(val value: Int) {
        Correct(0),
        Wrong(1)
    }

    var quizPos: Grid = Grid
    var status = Status.Correct

    abstract fun check()
    abstract fun getHintText(): String
}