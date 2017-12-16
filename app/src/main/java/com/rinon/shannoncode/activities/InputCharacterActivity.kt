package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TableRow

import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.ShannonCode
import com.rinon.shannoncode.activities.TopActivity.Companion.Type as Type
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
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                dialog.show(supportFragmentManager, null)
            }
            else if(!isCorrectProbability(pairList)) {
                // 確率の合計が100じゃない
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_probability_check))
                dialog.show(supportFragmentManager, null)
            }
            else if(!isCorrectCharacter(pairList)){
                // 重複した文字がある
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_unique_check))
                dialog.show(supportFragmentManager, null)
            }
            else {
                // 計算して画面遷移
                when(TopActivity.type) {
                    Type.Shannon -> {
                        val intent = Intent(this, QuizActivity::class.java)
                        intent.putExtra(QuizActivity.RESULT, convertToShannonCode(pairList))
                        startActivity(intent)
                    }
                    else -> {

                    }
                }

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

    private fun convertToShannonCode (pairList: ArrayList<Pair<EditText, EditText>>): ArrayList<ShannonCode.Content> {
        val contentList = ArrayList<ShannonCode.Content>()

        // 変換作業
        pairList.mapTo(contentList) {
            ShannonCode.Content(it.first.text.toString()[0],
                    it.second.text.toString().toInt())
        }
        return ShannonCode.calc(contentList)
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
