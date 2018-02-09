package com.rinon.shannoncode.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.rinon.shannoncode.R
import com.rinon.shannoncode.adapter.TutorialFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    companion object {
        private const val PAGE_NUM = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // ツールバーの設定
        toolbar.title = resources.getString(R.string.menu_how_to_use)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            view_pager.adapter = TutorialFragmentPagerAdapter(supportFragmentManager, PAGE_NUM)
        }

        // indicatorの設定
        indicator.setupWithViewPager(view_pager, true)

        // リスナー設定
        view_pager.addOnPageChangeListener(this)

        text_left.setOnClickListener {
            // 最初のページでなければ一つ戻る
            if(view_pager.currentItem != 0) {
                view_pager.currentItem = view_pager.currentItem - 1
            }
        }

        text_right.setOnClickListener {
            // 最後のページでなければ一つ進む
            if(view_pager.currentItem != PAGE_NUM - 1) {
                view_pager.currentItem = view_pager.currentItem + 1
            } else {
                finish()
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageSelected(position: Int) {
        text_left.text = when(position) {
            0 -> ""
            else -> resources.getString(R.string.back)
        }

        text_right.text = when(position) {
            PAGE_NUM - 1 -> resources.getString(R.string.start)
            else -> resources.getString(R.string.next)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}