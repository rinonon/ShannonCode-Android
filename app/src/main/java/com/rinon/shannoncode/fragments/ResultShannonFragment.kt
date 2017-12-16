package com.rinon.shannoncode.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode

import kotlinx.android.synthetic.main.fragment_result_shannon.*

/**
 * Created by rinon on 2017/11/20.
 */

class ResultShannonFragment : AbstractResultFragment() {

    companion object {
        fun getInstance(): ResultShannonFragment {
            return ResultShannonFragment()
        }

        enum class Order(val value: Int) {
            Text(0),
            Image(1),

            Max(2)
        }

        enum class TextOrder(val value: Int) {
            TextView(0),
            EditText(1),

            Max(2)
        }

        val QUIZ_START_INDEX_X = 1
        val QUIZ_START_INDEX_Y = ShannonCode.Order.PreProbability.value
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("log", "onCreateView")
        return inflater.inflate(R.layout.fragment_result_shannon, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        createResult()

        if(quizFlag) {
            setQuiz()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun createResult() {
        for((index, abstractContent) in contentList.withIndex()) {
            val row: LinearLayout = layoutInflater.inflate(R.layout.container_result_shannon, result_shannon, false) as LinearLayout
            val content = abstractContent as ShannonCode.Content

            for(order in 0 until ShannonCode.Order.Max.value) {
                var layout = row.getChildAt(order) as LinearLayout
                var viewSwitcher = layout.getChildAt(Order.Text.value) as ViewSwitcher
                var imageSwitcher = layout.getChildAt(Order.Image.value) as ImageSwitcher

                var str = when(order) {
                    ShannonCode.Order.Num.value -> (index + 1).toString()
                    ShannonCode.Order.Character.value -> content.char.toString()
                    ShannonCode.Order.Probability.value -> content.probability.toString()
                    ShannonCode.Order.PreProbability.value -> content.preProbability.toString()
                    ShannonCode.Order.Binary.value -> content.binaryText
                    ShannonCode.Order.Length.value -> content.length.toString()
                    ShannonCode.Order.Codeword.value -> content.codeword

                    else -> ""
                }

                (viewSwitcher.getChildAt(TextOrder.TextView.value) as TextView).text = str
                imageSwitcher.visibility = View.INVISIBLE
            }
            // 行を付け足す
            result_shannon.addView(row)
        }
    }

    private fun setQuiz() {
        quizPos.x = QUIZ_START_INDEX_X
        quizPos.y = QUIZ_START_INDEX_Y

        for (x in QUIZ_START_INDEX_X until contentList.size + 1) {
            for (y in QUIZ_START_INDEX_Y until ShannonCode.Order.Max.value) {

                var row = (result_shannon.getChildAt(x) as LinearLayout).getChildAt(y) as LinearLayout
                var viewSwitcher = row.getChildAt(Order.Text.value) as ViewSwitcher
                viewSwitcher.showNext()

                // TODO: ここの判定
                if (x > quizPos.x || y > quizPos.y) {
                    viewSwitcher.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun judge(): Boolean {

        var viewSwitcher = ((result_shannon.getChildAt(quizPos.x) as LinearLayout).getChildAt(quizPos.y) as LinearLayout).getChildAt(Order.Text.value) as ViewSwitcher
        var imageSwitcher = ((result_shannon.getChildAt(quizPos.x) as LinearLayout).getChildAt(quizPos.y) as LinearLayout).getChildAt(Order.Image.value) as ImageSwitcher
        val ans = (viewSwitcher.getChildAt(TextOrder.EditText.value) as EditText).text.toString()
        val content = contentList.get(quizPos.x - QUIZ_START_INDEX_X) as ShannonCode.Content

        val correct = when(quizPos.y) {
            ShannonCode.Order.PreProbability.value -> content.preProbability.toString()
            ShannonCode.Order.Binary.value -> content.binaryText
            ShannonCode.Order.Length.value -> content.length.toString()
            ShannonCode.Order.Codeword.value -> content.codeword
            else -> ""
        }

        imageSwitcher.visibility = View.VISIBLE

        if(ans == correct) {
            //correctマークをだし、次の問題へ
            if(status == Status.Wrong) {
                imageSwitcher.showNext()
            }
            viewSwitcher.showNext()
            status = Status.Correct
            quizPos.x++

            if(quizPos.x - QUIZ_START_INDEX_X >= contentList.size) {
                quizPos.x -= contentList.size
                quizPos.y++
            }

            if(quizPos.y >= ShannonCode.Order.Max.value) {
                // すべて正解
                return true
            }
            else {
                var x = quizPos.x
                var y = quizPos.y
                Log.d("pos", "x: $x, y:$y\n")
                viewSwitcher = ((result_shannon.getChildAt(quizPos.x) as LinearLayout).getChildAt(quizPos.y) as LinearLayout).getChildAt(Order.Text.value) as ViewSwitcher
                viewSwitcher.visibility = View.VISIBLE
            }

        }
        else {
            // wrongマークを出す
            if(status == Status.Correct) {
                imageSwitcher.showNext()
            }
            else {
                // TODO: 間違いがわかるアニメーション
            }
            status = Status.Wrong
        }
        return false
    }
}