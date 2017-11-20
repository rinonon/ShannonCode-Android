package com.rinon.shannoncode.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.activity_top.*

class TopActivity : AppCompatActivity() {

    companion object {
        enum class Type {
            Shannon,

            None
        }

        var type = Type.None
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        type = Type.Shannon

        top_button.setOnClickListener {
            val intent = Intent(this, InputNumberActivity::class.java)
            startActivity(intent)
        }
    }
}
