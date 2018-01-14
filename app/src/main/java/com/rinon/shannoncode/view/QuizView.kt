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

    private var mState = State.None
    private var mAnswer = ""
    private var mQuizFlag = false
    private var mHintTextId: Int? = null

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

    fun setHintTextId(id: Int) {
        mHintTextId = id
    }

    fun getHintTextId(): Int {
        return mHintTextId?: throw NullPointerException("hint id is null")
    }

    fun hide() {
        view_switcher.visibility = View.INVISIBLE
    }

    fun show() {
        view_switcher.visibility = View.VISIBLE
    }

    fun setQuiz(quiz: Boolean) {
        mQuizFlag = quiz

        if (quiz) {
            view_switcher.showNext()
        }
    }

    fun isQuiz(): Boolean {
        return mQuizFlag
    }

    fun check(): Boolean {
        return when (edit_text.text.toString() == mAnswer) {
            true -> {
                correct()
                true
            }
            false -> {
                wrong()
                false
            }
        }
    }

    private fun correct() {
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
        view_switcher.showNext()
    }

    private fun wrong() {
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