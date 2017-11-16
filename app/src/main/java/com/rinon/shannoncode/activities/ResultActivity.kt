package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableRow
import android.widget.TextView

import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    companion object {
        enum class Order(val value: Int) {
            Num(0),
            Character(1),
            Probability(2),
            PreProbability(3),
            Binary(4),
            Length(5),
            Codeword(6),

            Max(7)
        }

        val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = intent.getSerializableExtra(RESULT) as ShannonCode
        createRow(result)

        // リスナー設定
        encode_button.setOnClickListener {
            val intent = Intent(this, EncodeActivity::class.java)
            intent.putExtra(RESULT, result)
            startActivity(intent)
        }

        decode_button.setOnClickListener {

        }
    }

    private fun createRow(result: ShannonCode) {
        for((index, content) in result.contentList.withIndex()) {
            val row: TableRow = layoutInflater.inflate(R.layout.container_result, table_result, false) as TableRow

            // 文字列設定
            Log.d("num", Order.Num.value.toString())
            (row.getChildAt(Order.Num.value) as TextView).text = (index + 1).toString()
            (row.getChildAt(Order.Character.value) as TextView).text = content.char.toString()
            (row.getChildAt(Order.Probability.value) as TextView).text = content.probability.toString()
            (row.getChildAt(Order.PreProbability.value) as TextView).text = content.preProbability.toString()
            (row.getChildAt(Order.Binary.value) as TextView).text = content.binaryText
            (row.getChildAt(Order.Length.value) as TextView).text = content.length.toString()
            (row.getChildAt(Order.Codeword.value) as TextView).text = content.codeword

            // 行を付け足す
            table_result.addView(row)
        }
    }
}
