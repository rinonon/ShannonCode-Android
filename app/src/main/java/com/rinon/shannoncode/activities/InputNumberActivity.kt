package com.rinon.shannoncode.activities

import android.content.Intent
import kotlinx.android.synthetic.main.activity_input_number.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rinon.shannoncode.R
import com.rinon.shannoncode.dialogs.ErrorDialogFragment

class InputNumberActivity : AppCompatActivity() {
    companion object {
        val NUMBER = "number"
        val MAX_NUM = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_number)

        next_button.setOnClickListener {
            try {
                val num = char_num_text.text.toString().toIntOrNull() ?: throw Exception("null")

                if (num > MAX_NUM) {
                    // 数が最大値よりも大きい
                    val dialog = ErrorDialogFragment()
                    dialog.title = "Error"
                    dialog.message = "number should be less than $MAX_NUM"
                    dialog.show(supportFragmentManager, null)
                } else {
                    val intent = Intent(this, InputCharacterActivity::class.java)
                    intent.putExtra(NUMBER, num)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                val dialog = ErrorDialogFragment()
                dialog.title = "Error"
                dialog.message = "please input number"
                dialog.show(supportFragmentManager, null)
            }
        }
    }
}