package com.rinon.shannoncode.activities

import android.content.Intent
import kotlinx.android.synthetic.main.activity_input_number.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rinon.shannoncode.R
import com.rinon.shannoncode.managers.DialogManager

class InputNumberActivity : AppCompatActivity() {
    companion object {
        val NUMBER = "number"
        val MAX_NUM = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_number)

        next_button.setOnClickListener {
            val num = char_num_text.text.toString().toIntOrNull()

            when {
                num == null -> {
                    // 数字が入力されていない
                    val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                    dialog.show(supportFragmentManager, null)
                }
                num > MAX_NUM -> {
                    // 数が最大値よりも大きい
                    val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_num_check) + MAX_NUM)
                    dialog.show(supportFragmentManager, null)
                }
                else -> {
                    val intent = Intent(this, InputCharacterActivity::class.java)
                    intent.putExtra(NUMBER, num)
                    startActivity(intent)
                }
            }
        }
    }
}