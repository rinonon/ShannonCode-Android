package com.rinon.shannoncode.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rinon.shannoncode.R
import com.rinon.shannoncode.adapter.AlgorithmFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_description_algorithm.*

import com.rinon.shannoncode.activity.TopActivity.Companion.Type as Type

class AlgorithmDescriptionActivity : AppCompatActivity() {

    companion object {
        val KEY_TYPE = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode_decode)

        val type = intent.getSerializableExtra(KEY_TYPE) as Type

        // ツールバーの設定
        toolbar.title = "Algorithms"
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        view_pager.adapter = AlgorithmFragmentPagerAdapter(supportFragmentManager)

        // 初期ページ設定
        if (type == Type.None) {
            view_pager.currentItem = Type.Shannon.value
        } else {
            view_pager.currentItem = type.value
        }

        // tabの設定
        tabs.setViewPager(view_pager)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
