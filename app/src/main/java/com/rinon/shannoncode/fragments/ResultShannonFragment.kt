package com.rinon.shannoncode.fragments

import android.os.Bundle
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
                val layout = row.getChildAt(order) as LinearLayout
                val viewSwitcher = layout.getChildAt(Order.Text.value) as ViewSwitcher
                val imageSwitcher = layout.getChildAt(Order.Image.value) as ImageSwitcher

                val str = when(order) {
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

        for (x in quizPos.x until contentList.size + QUIZ_START_INDEX_X) {
            for (y in quizPos.y until ShannonCode.Order.Max.value) {

                val row = (result_shannon.getChildAt(x) as LinearLayout).getChildAt(y) as LinearLayout
                val viewSwitcher = row.getChildAt(Order.Text.value) as ViewSwitcher

                // ViewをEditTextにしておく
                viewSwitcher.showNext()

                if (x > quizPos.x || y > quizPos.y) {
                    // 一問目以外はまだ見えなくしておく
                    viewSwitcher.visibility = View.INVISIBLE
                }
            }
        }
    }

    // すべて正解ならtrueを返す
    override fun judge(): Boolean {
        var viewSwitcher = ((result_shannon.getChildAt(quizPos.x) as LinearLayout).getChildAt(quizPos.y) as LinearLayout).getChildAt(Order.Text.value) as ViewSwitcher
        val imageSwitcher = ((result_shannon.getChildAt(quizPos.x) as LinearLayout).getChildAt(quizPos.y) as LinearLayout).getChildAt(Order.Image.value) as ImageSwitcher
        val ans = (viewSwitcher.getChildAt(TextOrder.EditText.value) as EditText).text.toString()
        val content = contentList[quizPos.x - QUIZ_START_INDEX_X] as ShannonCode.Content

        val correct = when(quizPos.y) {
            ShannonCode.Order.PreProbability.value -> content.preProbability.toString()
            ShannonCode.Order.Binary.value -> content.binaryText
            ShannonCode.Order.Length.value -> content.length.toString()
            ShannonCode.Order.Codeword.value -> content.codeword
            else -> ""
        }

        // 正誤マークを出す
        imageSwitcher.visibility = View.VISIBLE

        if(ans == correct) {
            //correctマークに変える
            if(status == Status.Wrong) {
                imageSwitcher.showNext()
                status = Status.Correct
            }
            // EditTextからTextViewへ
            viewSwitcher.showNext()

            // 次の問題へ
            quizPos.x++

            if(quizPos.x - QUIZ_START_INDEX_X >= contentList.size) {
                quizPos.x -= contentList.size
                quizPos.y++
            }

            // すべて正解ならtrueを返す
            if(quizPos.y >= ShannonCode.Order.Max.value) {
                return true
            }
            else {
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

    override fun getHintText(): String {
        return when(quizPos.y) {
            ShannonCode.Order.PreProbability.value -> resources.getString(R.string.shannon_preprobability_hint)
            ShannonCode.Order.Binary.value -> resources.getString(R.string.shannon_binary_hint)
            ShannonCode.Order.Length.value -> resources.getString(R.string.shannon_length_hint)
            ShannonCode.Order.Codeword.value -> resources.getString(R.string.shannon_codeword_hint)

            else -> ""
        }
    }
}