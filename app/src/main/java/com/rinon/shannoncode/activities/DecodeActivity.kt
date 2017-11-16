package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.dialogs.ErrorDialogFragment
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_decode.*

class DecodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decode)

        val result = intent.getSerializableExtra(ResultActivity.RESULT) as ShannonCode

        encode_encode_button.setOnClickListener {
            val sourceText: String = decode_source_text.text.toString()
            val resultText = decode(result, sourceText)
            decode_result_text.text = resultText
        }
    }

    private fun decode(result: ShannonCode, sourceText: String): String {
        var ret = ""
        var currentIdx = 0

        while (currentIdx < sourceText.length) {
            try {
                val match: ShannonCode.Content = result.contentList.find {
                    it.codeword == sourceText.substring(currentIdx, it.codeword.length + currentIdx)
                } ?: throw Exception("not found")
                ret += match.char
                currentIdx += match.codeword.length
            } catch (e: Exception) {
                // エラー処理
                val dialog = ErrorDialogFragment()
                dialog.title = "Error"
                dialog.message = "wrong codeword found"
                dialog.show(supportFragmentManager, null)
                break
            }
        }
        return ret
    }
}