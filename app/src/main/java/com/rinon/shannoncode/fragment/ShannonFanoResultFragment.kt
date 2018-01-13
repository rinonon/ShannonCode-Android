package com.rinon.shannoncode.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.rinon.shannoncode.R
import com.rinon.shannoncode.model.ShannonFano
import com.rinon.shannoncode.view.QuizView
import kotlinx.android.synthetic.main.fragment_result_shannon_fano.*

class ShannonFanoResultFragment : AbstractResultFragment() {

    companion object {
        fun newInstance(codeList: Array<ShannonFano.Code>,
                        quizFlag: Boolean): ShannonFanoResultFragment {

            val instance = ShannonFanoResultFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CODE_LIST, codeList)
            bundle.putBoolean(KEY_QUIZ_FLAG, quizFlag)
            instance.arguments = bundle

            return instance
        }

        val QUIZ_START_INDEX_X = 2
        val QUIZ_START_INDEX_Y = 1

        private val KEY_QUIZ_FLAG = "quiz_flag"
        private val KEY_CODE_LIST = "code_list"

        // var codeList: Array<ShannonFano.Code>? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_result_shannon_fano, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizFlag = arguments?.getBoolean(KEY_QUIZ_FLAG) ?: throw NullPointerException("argument is null")
        val codeList = arguments?.getSerializable(KEY_CODE_LIST) as Array<ShannonFano.Code>

        setResult(codeList)

        if(quizFlag) {
            setQuiz()
        }
    }

    private fun setResult(codeList: Array<ShannonFano.Code>) {

        val context = context?: throw NullPointerException("context is null")
        val maxLength: Int = (codeList.maxBy { it.codeword.length })?.codeword?.length?: throw NullPointerException("maxLength is null")

        for(code in codeList) {
            val character = QuizView(context)
            character.setAnswer(code.char.toString())
            column_character.addView(character)

            val probability = QuizView(context)
            probability.setAnswer(code.probability.toString())
            column_probability.addView(probability)

            val codeword = QuizView(context)
            codeword.setAnswer(code.codeword)
            column_codeword.addView(codeword)
        }

        for(x in 0 until maxLength) {
            val column: LinearLayout = layoutInflater.inflate(R.layout.container_result_shannon_fano, container, false) as LinearLayout

            ((column.getChildAt(0) as LinearLayout).getChildAt(0) as TextView).text = (x + 1).toString()

            for(code in codeList) {
                val quiz = QuizView(context)
                quiz.setAnswer(code.codeword.substring(0, minOf(x + 1, code.codeword.length)))
                column.addView(quiz)
            }
            container.addView(column, QUIZ_START_INDEX_X + x)
        }
    }

    private fun setQuiz() {

    }

    override fun check(): Boolean {
       return true
    }

    override fun getHintText(): String {
        return ""
    }
}