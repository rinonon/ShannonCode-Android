package com.rinon.shannoncode.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.AbstractContent
import kotlinx.android.synthetic.main.activity_encode_decode.*

class EncodeDecodeActivity : AppCompatActivity() {

    companion object {
        val CONTENT = "content"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode_decode)

        val result = intent.getSerializableExtra(CONTENT) as ArrayList<AbstractContent>

        encode_button.setOnClickListener {
            val sourceText: String = encode_source_text.text.toString()
            val resultText = encode(result, sourceText)
            encode_decode_result_text.text = resultText
        }

        decode_button.setOnClickListener {
            val sourceText: String = encode_source_text.text.toString()
            val resultText = decode(result, sourceText)
            encode_decode_result_text.text = resultText
        }

        var codewordListStr = ""
        result.forEachIndexed {
            index, content -> codewordListStr += content.char + ":" + content.codeword +
                if(index + 1 % 5 == 0) "\n" else if(index + 1 == result.size) "" else ", "
        }
        encode_description_text.text = "($codewordListStr)"
    }

    private fun encode(result: ArrayList<AbstractContent>, sourceText: String): String {
        var ret = ""

        // 1文字ずつ変換
        for (char in sourceText) {
            try {
                val match: AbstractContent = result.find {
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

    private fun decode(result: ArrayList<AbstractContent>, sourceText: String): String {
        var ret = ""
        var currentIdx = 0

        while (currentIdx < sourceText.length) {
            try {
                val match: AbstractContent = result.find {
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
