package com.rinon.shannoncode.activity

import android.content.Intent
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
import com.rinon.shannoncode.helper.DialogHelper
import com.rinon.shannoncode.model.AbstractCode
import kotlinx.android.synthetic.main.activity_encode_decode.*

import com.rinon.shannoncode.activity.TopActivity.Companion.Type as Type

class EncodeDecodeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
                                                  EncodeFragmentListener,
                                                  DecodeFragmentListener {

    companion object {
        enum class Status(val value: Int) {
            Encode(0),
            Decode(1),

            None(-1)
        }

        const val KEY_CODE = "code"
        const val KEY_STATUS = "status"
        const val KEY_TYPE = "type"
        var type = TopActivity.Companion.Type.None
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encode_decode)

        val codeList = intent.getSerializableExtra(KEY_CODE) as Array<AbstractCode>
        val status = intent.getSerializableExtra(KEY_STATUS) as Status
        type = intent.getSerializableExtra(KEY_TYPE) as TopActivity.Companion.Type

        // ツールバーの設定
        toolbar.title = "Encode/Decode"
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open,
                R.string.drawer_close)

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
            R.id.menu_top -> {
                val intent = Intent(this, TopActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            R.id.menu_how_to_use -> {
                val intent = Intent(this, InformationActivity::class.java)
                intent.putExtra(InformationActivity.KEY_TYPE, InformationActivity.Companion.Type.HowToUse)
                startActivity(intent)
            }

            R.id.menu_about_this_app -> {
                val intent = Intent(this, InformationActivity::class.java)
                intent.putExtra(InformationActivity.KEY_TYPE, InformationActivity.Companion.Type.AboutThisApp)
                startActivity(intent)
            }

            R.id.menu_description -> {
                val intent = Intent(this, AlgorithmDescriptionActivity::class.java)
                intent.putExtra(AlgorithmDescriptionActivity.KEY_TYPE, type)
                startActivity(intent)
            }

            R.id.menu_shannon -> {
                val intent = Intent(this, ShannonCodingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            R.id.menu_shannon_fano -> {
                val intent = Intent(this, ShannonFanoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
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
        return true
    }

    // --- Fragment listener
    override fun encodeListener(event: EncodeFragment.Companion.Event) {
        when(event) {
            EncodeFragment.Companion.Event.EncodeError -> {
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_encode_check))
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun decodeListener(event: DecodeFragment.Companion.Event) {
        when(event) {
            DecodeFragment.Companion.Event.DecodeError -> {
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_decode_check))
                dialog.show(supportFragmentManager, null)
            }
        }
    }
}
