package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragments.ResultShannonFragment
import com.rinon.shannoncode.models.Content
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

        if(savedInstanceState == null) {
            when (TopActivity.type) {
                Type.Shannon -> {
                    val result = intent.getSerializableExtra(RESULT) as ArrayList<ShannonCode.Content>
                    val fragment = ResultShannonFragment.getInstance()
                    fragment.contentList = result
                    fragment.quizFlag = true

                    supportFragmentManager.beginTransaction()
                            .add(R.id.result_scroll, fragment)
                            .commit()
                }
            }

            // リスナー設定
            var contentList = intent.getSerializableExtra(RESULT) as ArrayList<Content>

            encode_button.setOnClickListener {
                val intent = Intent(this, EncodeActivity::class.java)
                intent.putExtra(RESULT, contentList)
                startActivity(intent)
            }

            decode_button.setOnClickListener {
                val intent = Intent(this, DecodeActivity::class.java)
                intent.putExtra(RESULT, contentList)
                startActivity(intent)
            }
        }
    }
}
