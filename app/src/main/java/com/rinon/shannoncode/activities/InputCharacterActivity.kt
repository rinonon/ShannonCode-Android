package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TableRow

import com.rinon.shannoncode.R
import com.rinon.shannoncode.dialogs.ErrorDialogFragment
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_input_character.*

class InputCharacterActivity : AppCompatActivity() {
    companion object {
        enum class Order(val value: Int) {
            Character(0),
            Probability(1),

            Max(2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_character)

        val num = intent.getIntExtra(InputNumberActivity.NUMBER, 0)
        val pairList = generateInputRows(num)

        // リスナー設定
        calc_button.setOnClickListener {
            if(!isInputAll(pairList)) {
                // 入力されていないところがある
                val dialog = ErrorDialogFragment()
                dialog.title = "Error"
                dialog.message = "please input all"
                dialog.show(supportFragmentManager, null)
            }
            else if(!isCorrectProbability(pairList)) {
                // 確率の合計が100じゃない
                val dialog = ErrorDialogFragment()
                dialog.title = "Error"
                dialog.message = "sum of probability should be 100"
                dialog.show(supportFragmentManager, null)
            }
            else if(!isCorrectCharacter(pairList)){
                // 重複した文字がある
                val dialog = ErrorDialogFragment()
                dialog.title = "Error"
                dialog.message = "character should be unique"
                dialog.show(supportFragmentManager, null)
            }
            else {
                val shannon = convertToShannonCode(pairList)
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(ResultActivity.RESULT, shannon)
                startActivity(intent)
            }
        }
    }

    private fun generateInputRows(num: Int):  ArrayList<Pair<EditText, EditText>> {
        val ret = ArrayList<Pair<EditText, EditText>>()        // first:char second:probability

        for(counter in 0 until num) {
            val row = layoutInflater.inflate(R.layout.container_input_character, scroll_view_content, false) as TableRow
            val char = row.getChildAt(Order.Character.value) as EditText
            val probability = row.getChildAt(Order.Probability.value) as EditText
            scroll_view_content.addView(row)

            ret.add(Pair(char, probability))
        }
        return ret
    }

    private fun convertToShannonCode (pairList: ArrayList<Pair<EditText, EditText>>): ShannonCode {
        val contentList = ArrayList<ShannonCode.Content>()

        // 変換作業
        pairList.mapTo(contentList) {
            ShannonCode.Content(it.first.text.toString()[0],
                    it.second.text.toString().toInt())
        }
        return ShannonCode(contentList)
    }

    private fun isInputAll(pairList: ArrayList<Pair<EditText, EditText>>): Boolean {
        return pairList.none { it.first.text.toString() == "" || it.second.text.toString() == "" }
    }

    private fun isCorrectProbability(pairList: ArrayList<Pair<EditText, EditText>>): Boolean {
        val sum = pairList.sumBy { it.second.text.toString().toInt() }
        return (sum == 100)
    }

    private fun isCorrectCharacter(pairList: ArrayList<Pair<EditText, EditText>>): Boolean {
        return pairList.size == pairList.distinctBy { it.first.text.toString()[0] }.size
    }
}
