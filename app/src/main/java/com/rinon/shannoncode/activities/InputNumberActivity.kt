package com.rinon.shannoncode.activities

import android.content.Intent
import kotlinx.android.synthetic.main.activity_input_number.*;
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rinon.shannoncode.R

class InputNumberActivity : AppCompatActivity() {

    companion object {
        val NUMBER = "number"
        val MAX_NUM = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_number)

        next_button.setOnClickListener {
            val num = char_num_text.text.toString().toInt()

            if(num > MAX_NUM) {
                // エラー処理
            }

            val intent = Intent(this, InputCharacterActivity::class.java)
            intent.putExtra(NUMBER, num)
            startActivity(intent)
        }
    }
}