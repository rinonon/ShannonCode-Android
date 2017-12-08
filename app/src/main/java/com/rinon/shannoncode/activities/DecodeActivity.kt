package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.Content
import kotlinx.android.synthetic.main.activity_decode.*

class DecodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decode)

        val result = intent.getSerializableExtra(ResultActivity.RESULT) as ArrayList<Content>

        decode_decode_button.setOnClickListener {
            val sourceText: String = decode_source_text.text.toString()
            val resultText = decode(result, sourceText)
            decode_result_text.text = resultText
        }

        var codewordListStr = ""
        result.forEachIndexed {
            index, content -> codewordListStr += content.char + ":" + content.codeword +
                if(index + 1 % 5 == 0) "\n" else if(index + 1 == result.size) "" else ", "
        }
        decode_description_text.text = "($codewordListStr)"
    }

    private fun decode(result: ArrayList<Content>, sourceText: String): String {
        var ret = ""
        var currentIdx = 0

        while (currentIdx < sourceText.length) {
            try {
                val match: Content = result.find {
                    it.codeword == sourceText.substring(currentIdx, it.codeword.length + currentIdx)
                } ?: throw Exception("not found")
                ret += match.char
                currentIdx += match.codeword.length
            } catch (e: Exception) {
                // エラー処理
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_decode_check))
                dialog.show(supportFragmentManager, null)
                return ""
            }
        }
        return ret
    }
}
