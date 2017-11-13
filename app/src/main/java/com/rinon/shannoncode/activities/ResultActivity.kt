package com.rinon.shannoncode.activities

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
        enum class Location(val value: Int) {
            Num(0),
            Character(1),
            Probability(2),
            PreProbability(3),
            Binary(4),
            Length(5),
            Codeword(6),

            Max(7)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = intent.getSerializableExtra(InputCharacterActivity.RESULT) as ShannonCode
        createRow(result)
    }

    private fun createRow(result: ShannonCode) {
        var counter = 0
        for(content in result.contentList) {

            var row: TableRow = layoutInflater.inflate(R.layout.container_result, null, false) as TableRow

            // 文字列設定
            Log.d("num", Location.Num.value.toString())
            (row.getChildAt(Location.Num.value) as TextView).setText((counter + 1).toString())
            (row.getChildAt(Location.Character.value) as TextView).setText(content.char.toString())
            (row.getChildAt(Location.Probability.value) as TextView).setText(content.probability.toString())
            (row.getChildAt(Location.PreProbability.value) as TextView).setText(content.preProbability.toString())
            (row.getChildAt(Location.Binary.value) as TextView).setText(content.binaryText)
            (row.getChildAt(Location.Length.value) as TextView).setText(content.length.toString())
            (row.getChildAt(Location.Codeword.value) as TextView).setText(content.codeword)

            // 行を付け足す
            table_result.addView(row)

            /*
            row.findViewById<TextView>(R.id.character_container_result).setText(content.char.toString())
            row.findViewById<TextView>(R.id.probability_container_result).setText(content.probability)
            row.findViewById<TextView>(R.id.preprobability_container_result).setText(content.preProbability)
            row.findViewById<TextView>(R.id.binary_container_result).setText(content.binaryText)
            row.findViewById<TextView>(R.id.length_container_result).setText(content.length)
            row.findViewById<TextView>(R.id.codeword_container_result).setText(content.codeword)
            */
        }
    }
}
