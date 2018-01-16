package com.rinon.shannoncode.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.rinon.shannoncode.R
import com.rinon.shannoncode.model.ShannonCode
import com.rinon.shannoncode.view.QuizView
import kotlinx.android.synthetic.main.fragment_result_shannon.*
import java.util.*

import com.rinon.shannoncode.fragment.ResultFragment.Companion.QuizType as QuizType
import com.rinon.shannoncode.fragment.ResultFragment.Companion.Event as Event

class ShannonResultFragment : AbstractResultFragment() {

    companion object {
        fun newInstance(codeList: Array<ShannonCode.Code>,
                        quizType: QuizType): ShannonResultFragment {

            val instance = ShannonResultFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CONTENT_LIST, codeList)
            bundle.putSerializable(KEY_QUIZ_TYPE, quizType)
            instance.arguments = bundle

            return instance
        }

        private val KEY_QUIZ_TYPE = "quiz_type"
        private val KEY_CONTENT_LIST = "content_list"
        private val quizList = mutableListOf<QuizView>()

        private var quizIndex = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_result_shannon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizType = arguments?.getSerializable(KEY_QUIZ_TYPE) as QuizType
        val codeList = arguments?.getSerializable(KEY_CONTENT_LIST) as Array<ShannonCode.Code>

        setResult(codeList)
        setQuiz(codeList, quizType)
    }

    private fun setResult(codeList: Array<ShannonCode.Code>) {
        val context = context?: throw NullPointerException("context is null")
        quizList.clear()

        for((index, code) in codeList.withIndex()) {
            val row = layoutInflater.inflate(R.layout.container_result_shannon, container, false) as LinearLayout

            for(order in ShannonCode.Order.Num.value until ShannonCode.Order.Max.value) {
                val answer = when(order) {
                    ShannonCode.Order.Num.value -> (index + 1).toString()
                    ShannonCode.Order.Character.value -> (code.char).toString()
                    ShannonCode.Order.Probability.value -> (code.probability).toString()
                    ShannonCode.Order.PreProbability.value -> (code.preProbability).toString()
                    ShannonCode.Order.Binary.value -> code.binaryText
                    ShannonCode.Order.Length.value -> (code.length).toString()
                    ShannonCode.Order.Codeword.value -> code.codeword

                    else -> throw IllegalArgumentException("order is illegal")
                }

                val quiz = QuizView(context)
                quiz.setAnswer(answer)
                quiz.setBackgroundColor(resources.getColor(R.color.table_normal))

                quiz.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1.0f)

                // 表用のマージン設定
                quiz.setMargin(
                        1,
                        1,
                        if(index == codeList.size - 1) 1 else 0,
                        if(order == ShannonCode.Order.Max.value - 1) 1 else 0)

                row.addView(quiz)

                when(order) {
                    ShannonCode.Order.PreProbability.value -> quizList.add(quiz)
                    ShannonCode.Order.Binary.value -> quizList.add(quiz)
                    ShannonCode.Order.Length.value -> quizList.add(quiz)
                    ShannonCode.Order.Codeword.value -> quizList.add(quiz)
                }
            }
            // 行を付け足す
            container.addView(row)
        }
    }

    private fun setQuiz(codeList: Array<ShannonCode.Code>, quizType: QuizType) {
        val quizRate = (when(quizType) {
            QuizType.Easy -> resources.getInteger(R.integer.quiz_rate_easy)
            QuizType.Normal -> resources.getInteger(R.integer.quiz_rate_normal)
            QuizType.Hard -> resources.getInteger(R.integer.quiz_rate_hard)
            else -> return
        }) / 100.0

        // クイズフラグ設定
        val rowNum = ShannonCode.Order.Codeword.value - ShannonCode.Order.Probability.value
        val quizNum = (Math.ceil(rowNum * quizRate)).toInt()

        for(index in 0 until codeList.size) {
            var count = 0
            while(count < quizNum) {
                val random = Random().nextInt(rowNum)

                if(!quizList[random + rowNum * index].isQuiz()) {
                    quizList[random + rowNum * index].setQuiz(true)
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