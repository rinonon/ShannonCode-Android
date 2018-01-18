package com.rinon.shannoncode.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragment.TopMenuFragment
import com.rinon.shannoncode.fragment.TopMenuFragmentListener
import kotlinx.android.synthetic.main.activity_top.*

class TopActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener
                                        , TopMenuFragmentListener {

    companion object {
        enum class Type(val value: Int) {
            Shannon(0),
            ShannonFano(1),

            None(-1)
        }
    }

    var type = Type.None

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

        navigation_view.setNavigationItemSelectedListener(this)

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
            R.id.menu_top -> {

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

            R.id.menu_overview -> {
                val intent = Intent(this, AlgorithmDescriptionActivity::class.java)
                intent.putExtra(AlgorithmDescriptionActivity.KEY_TYPE, Type.None)
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
        when(item?.itemId) {
            R.id.action_settings -> {

            }
        }
        return true
    }

    // --- Fragment listener ---
    override fun topMenuListener(event: TopMenuFragment.Companion.Event) {
        when(event) {
            TopMenuFragment.Companion.Event.Help -> {
                val intent = Intent(this, InformationActivity::class.java)
                intent.putExtra(InformationActivity.KEY_TYPE, InformationActivity.Companion.Type.HowToUse)
                startActivity(intent)
            }

            TopMenuFragment.Companion.Event.Shannon -> {
                type = Type.Shannon

                //val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair(view, view.transitionName))
                val intent = Intent(this, ShannonCodingActivity::class.java)
                //startActivity(intent, optionCompat.toBundle())
                startActivity(intent)
            }

            TopMenuFragment.Companion.Event.ShannonFano -> {
                type = Type.ShannonFano

                val intent = Intent(this, ShannonFanoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
