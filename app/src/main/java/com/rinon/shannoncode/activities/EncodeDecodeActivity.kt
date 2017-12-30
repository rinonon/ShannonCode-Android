package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.rinon.shannoncode.R
import com.rinon.shannoncode.adapters.EncodeDecodeFragmentPagerAdapter
import com.rinon.shannoncode.fragments.DecodeFragment
import com.rinon.shannoncode.fragments.DecodeFragmentListener
import com.rinon.shannoncode.fragments.EncodeFragment
import com.rinon.shannoncode.fragments.EncodeFragmentListener
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.models.AbstractContent
import kotlinx.android.synthetic.main.activity_encode_decode.*

class EncodeDecodeActivity : AppCompatActivity(), EncodeFragmentListener,
                                                  DecodeFragmentListener {

    companion object {
        enum class Status(val value: Int) {
            Encode(0),
            Decode(1),

            None(-1);
        }

        val CONTENT = "content"
        val STATUS = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode_decode)

        val contentList = intent.getSerializableExtra(CONTENT) as ArrayList<AbstractContent>
        val status = intent.getSerializableExtra(STATUS) as Status

        // ツールバーの設定
        toolbar.title = "Encode/Decode"
        setSupportActionBar(toolbar)

        // tabの設定
        view_pager.adapter = EncodeDecodeFragmentPagerAdapter(supportFragmentManager, contentList)
        tabs.setViewPager(view_pager)

        // 初期ページ設定
        if (status == Status.None) {
            throw IllegalArgumentException("Status is none")
        } else {
            view_pager.currentItem = status.value
        }
    }

    override fun encodeListener(event: EncodeFragment.Companion.Event) {
        when(event) {
            EncodeFragment.Companion.Event.EncodeError -> {
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_encode_check))
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun decodeListener(event: DecodeFragment.Companion.Event) {
        when(event) {
            DecodeFragment.Companion.Event.DecodeError -> {
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_decode_check))
                dialog.show(supportFragmentManager, null)
            }
        }
    }
}
