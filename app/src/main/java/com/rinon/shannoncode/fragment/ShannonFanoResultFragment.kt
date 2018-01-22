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
import java.util.*

import com.rinon.shannoncode.fragment.ResultFragment.Companion.QuizType as QuizType
import com.rinon.shannoncode.fragment.ResultFragment.Companion.Event as Event

class ShannonFanoResultFragment : AbstractResultFragment() {

    companion object {
        fun newInstance(codeList: Array<ShannonFano.Code>,
                        quizType: QuizType): ShannonFanoResultFragment {

            val instance = ShannonFanoResultFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CODE_LIST, codeList)
            bundle.putSerializable(KEY_QUIZ_TYPE, quizType)
            instance.arguments = bundle

            return instance
        }

        private const val QUIZ_START_INDEX_X = 2
        private const val KEY_QUIZ_TYPE = "quiz_type"
        private const val KEY_CODE_LIST = "code_list"
        private val quizList = mutableListOf<QuizView>()

        private var quizIndex = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_result_shannon_fano, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizType = arguments?.getSerializable(KEY_QUIZ_TYPE) as QuizType
        val codeList = arguments?.getSerializable(KEY_CODE_LIST) as Array<ShannonFano.Code>

        setResult(codeList)
        setQuiz(codeList, quizType)
    }

    private fun setResult(codeList: Array<ShannonFano.Code>) {
        val context = context?: throw NullPointerException("context is null")
        val maxLength: Int = (codeList.maxBy { it.codeword.length })?.codeword?.length?: throw NullPointerException("maxLength is null")
        quizList.clear()

        // 列追加
        for(x in 0 until maxLength) {
            val column: LinearLayout = layoutInflater.inflate(R.layout.container_result_shannon_fano, container, false) as LinearLayout
            (column.getChildAt(0) as TextView).text = (x + 1).toString()

            for(code in codeList) {
                val quiz = QuizView(context)
                quiz.setAnswer(code.codeword.substring(0, minOf(x + 1, code.codeword.length)))
                quiz.setHintTextId(R.string.hint_shannon_fano)
                quiz.setBackgroundColor(resources.getColor(R.color.table_normal))

                quiz.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1.0f)

                // 表用のマージン設定
                quiz.setMargin(
                        0,
                        1,
                        0,
                        0)

                column.addView(quiz)
                quizList.add(quiz)
            }
            container.addView(column, QUIZ_START_INDEX_X + x)
        }

        // 文字、確率、符号設定
        for(code in codeList) {
            for(order in ShannonFano.Order.Character.value until ShannonFano.Order.Max.value) {
                val answer = when(order) {
                    ShannonFano.Order.Character.value -> code.symbol.toString()
                    ShannonFano.Order.Probability.value -> code.probability.toString()
                    ShannonFano.Order.Codeword.value -> code.codeword

                    else -> throw IllegalArgumentException("order is illegal")
                }

                val quiz = QuizView(context)
                quiz.setAnswer(answer)
                quiz.setHintTextId(R.string.hint_shannon_fano)
                quiz.setBackgroundColor(resources.getColor(R.color.table_normal))

                quiz.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1.0f)

                // 表用のマージン設定
                quiz.setMargin(
                        0,
                        1,
                        0,
                        0)

                when(order) {
                    ShannonFano.Order.Character.value -> {
                        column_character.addView(quiz)
                    }
                    ShannonFano.Order.Probability.value -> {
                        column_probability.addView(quiz)
                    }
                    ShannonFano.Order.Codeword.value -> {
                        column_codeword.addView(quiz)
                        quizList.add(quiz)      // codewordはクイズにいれる
                    }
                }
            }
        }
    }

    private fun setQuiz(codeList: Array<ShannonFano.Code>, quizType: QuizType) {
        val quizRate = (when(quizType) {
            QuizType.Easy -> resources.getInteger(R.integer.quiz_rate_easy)
            QuizType.Normal -> resources.getInteger(R.integer.quiz_rate_normal)
            QuizType.Hard -> resources.getInteger(R.integer.quiz_rate_hard)
            else -> return
        }) / 100.0

        val quizNum = (Math.ceil(codeList.size * quizRate)).toInt()
        val maxLength: Int = (codeList.maxBy { it.codeword.length })?.codeword?.length?: throw NullPointerException("maxLength is null")

        // クイズフラグ設定 codewordの分+1
        for(index in 0 until maxLength + 1) {
            var count = 0
            while(count < quizNum) {
                val random = Random().nextInt(codeList.size)

                if(!quizList[random + codeList.size * index].isQuiz()) {
                    quizList[random + codeList.size * index].setQuiz(true)
                    count++
                }
            }
        }

        // 初期化
        var setFlag = false
        for((index, quiz) in quizList.withIndex()) {
            if(!quiz.isQuiz()) {
                continue
            } else if(!setFlag) {
                quiz.show()
                setFlag = true
                quizIndex = index
            } else {
                quiz.hide()
            }
        }
    }

    override fun check() {
        if(quizList[quizIndex].check()) {
            // correct
            quizIndex++
            while(true) {
                if(quizIndex >= quizList.size) {
                    // 全問正解
                    (parentFragment as ResultFragment).eventListener(Event.Complete)
                    break
                }

                if(quizList[quizIndex].isQuiz()) {
                    quizList[quizIndex].show()
                    break
                } else {
                    quizIndex++
                    continue
                }
            }

        } else {
            // wrong
            (parentFragment as ResultFragment).eventListener(Event.Wrong)
        }
    }

    override fun getHintText(): String {
        return resources.getString(quizList[quizIndex].getHintTextId())
    }
}