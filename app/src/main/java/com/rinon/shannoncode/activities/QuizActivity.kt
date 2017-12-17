package com.rinon.shannoncode.activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragments.AbstractResultFragment
import com.rinon.shannoncode.fragments.ResultShannonFragment
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.AbstractContent
import kotlinx.android.synthetic.main.activity_quiz.*
import com.rinon.shannoncode.activities.TopActivity.Companion.Type as Type

class QuizActivity : AppCompatActivity() {

    companion object {
        var fragment: AbstractResultFragment? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val contentList = intent.getSerializableExtra(ResultActivity.RESULT) as ArrayList<AbstractContent>

        judge_button.setOnClickListener {
            val allComplete = fragment?.judge()
            if (allComplete != null && allComplete) {
                // すべて正解なら画面遷移し、ダイアログをだす
                val dialog = DialogManager.createSimpleDialog(resources.getString(R.string.congratulation),
                                                              resources.getString(R.string.all_correct),
                                                              DialogInterface.OnClickListener { dialog, id ->
                                                                  val intent = Intent(this, ResultActivity::class.java)
                                                                  intent.putExtra(ResultActivity.RESULT, contentList)
                                                                  startActivity(intent)
                                                              })
                dialog.show(supportFragmentManager, null)
            }
        }

        hint_button.setOnClickListener {
            val hintText = fragment?.getHintText()

            if(hintText != null) {
                val dialog = DialogManager.createSimpleDialog("Hint", hintText)
                dialog.show(supportFragmentManager, null)
            }
            else {
                // ありえないのでエラー処理
            }
        }

        if(savedInstanceState == null) {
            when (TopActivity.type) {
                Type.Shannon -> {
                    val result = intent.getSerializableExtra(ResultActivity.RESULT) as ArrayList<AbstractContent>
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
