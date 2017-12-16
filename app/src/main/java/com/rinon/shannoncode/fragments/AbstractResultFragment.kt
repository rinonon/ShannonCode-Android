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

        fun reset() {
            x = 0
            y = 0
        }
    }

    var quizPos: Grid = Grid
    var quizFlag: Boolean = false
    var contentList: ArrayList<AbstractContent> = ArrayList()

    abstract fun judge()
}