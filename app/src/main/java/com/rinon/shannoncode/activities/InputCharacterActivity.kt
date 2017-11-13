package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_input_character.*

class InputCharacterActivity : AppCompatActivity() {
    companion object {
        var pairList = ArrayList<Pair<EditText, EditText>>()        // first:char second:probability
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
            val row = layoutInflater.inflate(R.layout.container_input_character, scroll_view_content, true)
            val char = row.findViewById<EditText>(R.id.edit_character)
            val probability = row.findViewById<EditText>(R.id.edit_probability)

            // TODO: 処理の仕方がわからなすぎるのでとりあえずはこれ
            when(counter) {
                0 -> {
                    char.id = R.id.character_input_character0
                    probability.id = R.id.probability_input_character0
                }
                1 -> {
                    char.id = R.id.character_input_character1
                    probability.id = R.id.probability_input_character1
                }
                2 -> {
                    char.id = R.id.character_input_character2
                    probability.id = R.id.probability_input_character2
                }
                3 -> {
                    char.id = R.id.character_input_character3
                    probability.id = R.id.probability_input_character3
                }
                4 -> {
                    char.id = R.id.character_input_character4
                    probability.id = R.id.probability_input_character4
                }
                5 -> {
                    char.id = R.id.character_input_character5
                    probability.id = R.id.probability_input_character5
                }
                6 -> {
                    char.id = R.id.character_input_character6
                    probability.id = R.id.probability_input_character6
                }
                7 -> {
                    char.id = R.id.character_input_character7
                    probability.id = R.id.probability_input_character7
                }
                8 -> {
                    char.id = R.id.character_input_character8
                    probability.id = R.id.probability_input_character8
                }
                9 -> {
                    char.id = R.id.character_input_character9
                    probability.id = R.id.probability_input_character9
                }
                else -> {
                    // ありえないのでエラー処理
                }
            }
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
