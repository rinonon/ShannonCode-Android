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

        // ツールバーの設定
        toolbar.title = resources.getString(R.string.top_menu)
        setSupportActionBar(toolbar)

        button_shannon_coding.setOnClickListener {
            type = Type.Shannon

            val intent = Intent(this, ShannonCodingActivity::class.java)
            startActivity(intent)
        }
    }
}
