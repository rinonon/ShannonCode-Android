package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragments.AbstractResultFragment
import com.rinon.shannoncode.fragments.ResultShannonFragment
import com.rinon.shannoncode.models.AbstractContent
import kotlinx.android.synthetic.main.activity_quiz.*
import com.rinon.shannoncode.activities.TopActivity.Companion.Type as Type

class QuizActivity : AppCompatActivity() {

    companion object {
        val RESULT = "result"
        var fragment: AbstractResultFragment? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        var contentList = intent.getSerializableExtra(RESULT) as ArrayList<AbstractContent>

        judge_button.setOnClickListener {
            fragment?.judge()
        }

        if(savedInstanceState == null) {
            when (TopActivity.type) {
                Type.Shannon -> {
                    val result = intent.getSerializableExtra(RESULT) as ArrayList<AbstractContent>
                    fragment = ResultShannonFragment.getInstance()
                    fragment?.contentList = result
                    fragment?.quizFlag = true

                    supportFragmentManager.beginTransaction()
                            .add(R.id.result_scroll, fragment)
                            .commit()
                }
            }
        }
    }
}
