package com.rinon.shannoncode.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode

import kotlinx.android.synthetic.main.fragment_result_shannon.*

/**
 * Created by rinon on 2017/11/20.
 */

class ResultShannonFragment : Fragment() {

    var contentList: ArrayList<ShannonCode.Content> = ArrayList()
    var quizFlag: Boolean = false

    companion object {
        fun getInstance(): ResultShannonFragment {
            return ResultShannonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("log", "onCreateView")
        return inflater.inflate(R.layout.fragment_result_shannon, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        createRow(contentList)

        if(quizFlag) {
            hideResult()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun createRow(contentList: ArrayList<ShannonCode.Content>) {
        for((index, content) in contentList.withIndex()) {
            val row: LinearLayout = layoutInflater.inflate(R.layout.container_result_shannon, result_shannon, false) as LinearLayout

            // 文字列設定
            (row.getChildAt(ShannonCode.Order.Num.value) as TextView).text = (index + 1).toString()
            (row.getChildAt(ShannonCode.Order.Character.value) as TextView).text = content.char.toString()
            (row.getChildAt(ShannonCode.Order.Probability.value) as TextView).text = content.probability.toString()
            (row.getChildAt(ShannonCode.Order.PreProbability.value) as TextView).text = content.preProbability.toString()
            (row.getChildAt(ShannonCode.Order.Binary.value) as TextView).text = content.binaryText
            (row.getChildAt(ShannonCode.Order.Length.value) as TextView).text = content.length.toString()
            (row.getChildAt(ShannonCode.Order.Codeword.value) as TextView).text = content.codeword

            // 行を付け足す
            result_shannon.addView(row)
        }
    }

    fun hideResult() {
        for(x in 1..contentList.size) {
            val column = result_shannon.getChildAt(x) as LinearLayout

            for(y in 0..ShannonCode.Order.Max.value - 1) {
                val row = column.getChildAt(y)
                row.visibility = View.INVISIBLE
            }
        }
    }
}