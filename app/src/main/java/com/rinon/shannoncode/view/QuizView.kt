package com.rinon.shannoncode.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.container_quiz.view.*


class QuizView : LinearLayout {

    companion object {

        enum class State {
            Correct,
            Wrong,

            None
        }
    }

    var mState = State.None
    var mAnswer = ""
    var mQuizFlag = false


    constructor(context: Context) : super(context) {
        prepare(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare(context)
    }

    private fun prepare(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.container_quiz, this, true)

        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                0,
                                                1.0f)

    }

    fun setAnswer(answer: String) {
        mAnswer = answer
        text_answer.text = answer
    }

    fun correct() {
        when(mState) {
            State.Correct -> {
                // なにもしない
            }

            State.Wrong -> {
                image_switcher.showNext()
            }

            State.None -> {
                image_switcher.visibility = View.VISIBLE
            }
        }
        mState = State.Correct
    }

    fun wrong() {
        when(mState) {
            State.Correct -> {
                image_switcher.showNext()
            }

            State.Wrong -> {
                // なにもしない
            }

            State.None -> {
                image_switcher.visibility = View.VISIBLE
                image_switcher.showNext()
            }
        }
        mState = State.Wrong
    }
}