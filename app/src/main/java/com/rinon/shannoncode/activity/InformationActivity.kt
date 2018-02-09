package com.rinon.shannoncode.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragment.AboutThisAppFragment
import com.rinon.shannoncode.fragment.HowToUseFragment
import kotlinx.android.synthetic.main.activity_information.*

class InformationActivity : AppCompatActivity() {

    companion object {
        enum class Type {
            AboutThisApp,
            HowToUse,

            None
        }

        const val KEY_TYPE = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val type = intent.getSerializableExtra(KEY_TYPE) as Type

        // ツールバーの設定
        val title = when(type) {
            Type.AboutThisApp -> resources.getString(R.string.menu_about_this_app)
            Type.HowToUse -> resources.getString(R.string.menu_how_to_use)

            else -> ""
        }

        toolbar.title = title
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState == null) {
            val fragment = when (type) {
                Type.HowToUse -> HowToUseFragment.newInstance()
                Type.AboutThisApp -> AboutThisAppFragment.newInstance()
                else -> throw IllegalArgumentException("type is illegal")
            }

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.tag)
                    .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}