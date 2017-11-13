package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TableRow
import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_input_character.*

class InputCharacterActivity : AppCompatActivity() {
    companion object {
        enum class Location(val value: Int) {
            Character(0),
            Probability(1),

            Max(2)
        }

        val pairList = ArrayList<Pair<EditText, EditText>>()        // first:char second:probability
        val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_character)

        calc_button.setOnClickListener {
            val shannon = calc()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(RESULT, shannon)
            startActivity(intent)
        }

        val num = intent.getIntExtra(InputNumberActivity.NUMBER, 0)

        for(counter in 0 until num) {
            val row = layoutInflater.inflate(R.layout.container_input_character, scroll_view_content, false) as TableRow
            val char = row.getChildAt(Location.Character.value) as EditText
            val probability = row.getChildAt(Location.Probability.value) as EditText
            scroll_view_content.addView(row)

            pairList.add(Pair<EditText, EditText>(char, probability))
        }
    }

    private fun calc (): ShannonCode {
        val contentList = ArrayList<ShannonCode.Content>()

        // 変換作業
        pairList.mapTo(contentList) {
            ShannonCode.Content(it.first.text.toString()[0],
                    it.second.text.toString().toInt())
        }
        val ret = ShannonCode(contentList)
        ret.calc()

        return ret
    }
}
