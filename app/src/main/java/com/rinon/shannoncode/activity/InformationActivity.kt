package com.rinon.shannoncode.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragment.AboutThisAppFragment
import kotlinx.android.synthetic.main.activity_information.*

class InformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        // ツールバーの設定
        val title = resources.getString(R.string.menu_about_this_app)
        toolbar.title = title
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState == null) {
            val fragment = AboutThisAppFragment.newInstance()

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.tag)
                    .commitAllowingStateLoss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}