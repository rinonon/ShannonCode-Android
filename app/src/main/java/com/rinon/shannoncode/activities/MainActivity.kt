package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rinon.shannoncode.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_number)
    }

    fun goInputCharacter(view: View?) {
        setContentView(R.layout.input_character);
    }
}