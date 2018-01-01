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
import com.rinon.shannoncode.fragment.TopMenuFragment
import com.rinon.shannoncode.fragment.TopMenuFragmentListener
import kotlinx.android.synthetic.main.activity_top.*

class TopActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener
                                        , TopMenuFragmentListener {

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

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.top_menu,
                R.string.shannon_coding)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null) {
            val fragment = TopMenuFragment.newInstance()

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.tag)
                    .commit()
        }
    }

    // --- Navigation View ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item1 -> {
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

    override fun topMenuListener(event: TopMenuFragment.Companion.Event) {
        when(event) {
            TopMenuFragment.Companion.Event.Shannon -> {
                type = Type.Shannon

                val intent = Intent(this, ShannonCodingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
