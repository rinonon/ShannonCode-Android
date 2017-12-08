package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.Content
import kotlinx.android.synthetic.main.activity_encode.*

class EncodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode)

        val result = intent.getSerializableExtra(ResultActivity.RESULT) as ArrayList<Content>

        encode_encode_button.setOnClickListener {
            val sourceText: String = encode_source_text.text.toString()
            val resultText = encode(result, sourceText)
            encode_result_text.text = resultText
        }
    }

    private fun encode(result: ArrayList<Content>, sourceText: String): String {
        var ret = ""

        // 1文字ずつ変換
        for (char in sourceText) {
            try {
                val match: Content = result.find {
                    it.char == char
                } ?: throw Exception("not found")
                ret += match.codeword

            } catch (e: Exception) {
                // エラー処理
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_encode_check))
                dialog.show(supportFragmentManager, null)
                return ""
            }
        }
        return ret
    }
}
