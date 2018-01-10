package com.rinon.shannoncode.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem

import com.rinon.shannoncode.R
import com.rinon.shannoncode.adapter.EncodeDecodeFragmentPagerAdapter
import com.rinon.shannoncode.fragment.DecodeFragment
import com.rinon.shannoncode.fragment.DecodeFragmentListener
import com.rinon.shannoncode.fragment.EncodeFragment
import com.rinon.shannoncode.fragment.EncodeFragmentListener
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.model.AbstractCode
import kotlinx.android.synthetic.main.activity_encode_decode.*

class EncodeDecodeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
                                                  EncodeFragmentListener,
                                                  DecodeFragmentListener {

    companion object {
        enum class Status(val value: Int) {
            Encode(0),
            Decode(1),

            None(-1);
        }

        val CODE = "code"
        val STATUS = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode_decode)

        val codeList = intent.getSerializableExtra(CODE) as Array<AbstractCode>
        val status = intent.getSerializableExtra(STATUS) as Status

        // ツールバーの設定
        toolbar.title = "Encode/Decode"
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.top_menu,
                R.string.shannon_coding)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        view_pager.adapter = EncodeDecodeFragmentPagerAdapter(supportFragmentManager, codeList)

        // 初期ページ設定
        if (status == Status.None) {
            throw IllegalArgumentException("Status is none")
        } else {
            view_pager.currentItem = status.value
        }

        // tabの設定
        tabs.setViewPager(view_pager)
    }


    // --- Navigation View ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item1 -> {
            }

            R.id.menu_shannon -> {

            }

            else -> {
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_settings -> {

            }
        }
        return true
    }

    // --- Fragment listener
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
