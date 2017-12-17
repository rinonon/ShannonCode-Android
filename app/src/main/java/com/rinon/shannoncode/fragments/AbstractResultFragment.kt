package com.rinon.shannoncode.fragments

import android.support.v4.app.Fragment
import com.rinon.shannoncode.models.AbstractContent

/**
 * Created by rinon on 2017/12/16.
 */

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
    var quizFlag: Boolean = false
    var status = Status.Correct
    var contentList: ArrayList<AbstractContent> = ArrayList()

    abstract fun judge(): Boolean
    abstract fun getHintText(): String
}