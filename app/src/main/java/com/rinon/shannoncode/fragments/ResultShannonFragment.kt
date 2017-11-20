package com.rinon.shannoncode.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.rinon.shannoncode.R
import com.rinon.shannoncode.activities.ResultActivity
import com.rinon.shannoncode.models.ShannonCode

import kotlinx.android.synthetic.main.fragment_result_shannon.*

/**
 * Created by rinon on 2017/11/20.
 */

class ResultShannonFragment : Fragment() {

    var contentList: ArrayList<ShannonCode.Content> = ArrayList()

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
        super.onViewCreated(view, savedInstanceState)
        createRow(contentList)
    }


    private fun createRow(contentList: ArrayList<ShannonCode.Content>) {
        for((index, content) in contentList.withIndex()) {
            val row: TableRow = layoutInflater.inflate(R.layout.container_result, table_result, false) as TableRow

            // 文字列設定
            Log.d("num", ResultActivity.Companion.Order.Num.value.toString())
            (row.getChildAt(ResultActivity.Companion.Order.Num.value) as TextView).text = (index + 1).toString()
            (row.getChildAt(ResultActivity.Companion.Order.Character.value) as TextView).text = content.char.toString()
            (row.getChildAt(ResultActivity.Companion.Order.Probability.value) as TextView).text = content.probability.toString()
            (row.getChildAt(ResultActivity.Companion.Order.PreProbability.value) as TextView).text = content.preProbability.toString()
            (row.getChildAt(ResultActivity.Companion.Order.Binary.value) as TextView).text = content.binaryText
            (row.getChildAt(ResultActivity.Companion.Order.Length.value) as TextView).text = content.length.toString()
            (row.getChildAt(ResultActivity.Companion.Order.Codeword.value) as TextView).text = content.codeword

            // 行を付け足す
            table_result.addView(row)
        }
    }
}