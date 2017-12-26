package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragments.ResultShannonFragment
import com.rinon.shannoncode.models.AbstractContent
import com.rinon.shannoncode.models.ShannonCode
import com.rinon.shannoncode.activities.TopActivity.Companion.Type as Type
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    companion object {
        val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        result_scroll.setOnTouchListener { v, event ->
            v.requestFocusFromTouch()
            false
        }

        val contentList = intent.getSerializableExtra(RESULT) as ArrayList<AbstractContent>

        // リスナー設定
        encode_decode_button.setOnClickListener {
            val intent = Intent(this, EncodeDecodeActivity::class.java)
            intent.putExtra(RESULT, contentList)
            startActivity(intent)
        }

        top_button.setOnClickListener {
            val intent = Intent(this, TopActivity::class.java)
            startActivity(intent)
        }

        if(savedInstanceState == null) {
            when (TopActivity.type) {
                Type.Shannon -> {
                    val result = intent.getSerializableExtra(RESULT) as ArrayList<ShannonCode.Content>
                    val fragment = ResultShannonFragment.getInstance()

                    val bundle = Bundle()
                    bundle.putSerializable(ResultShannonFragment.KEY_CONTENT_LIST, result)
                    bundle.putBoolean(ResultShannonFragment.KEY_QUIZ_FLAG, true)
                    QuizActivity.fragment?.arguments = bundle

                    supportFragmentManager.beginTransaction()
                            .add(R.id.result_scroll, fragment)
                            .commit()
                }
            }
        }
    }
}
