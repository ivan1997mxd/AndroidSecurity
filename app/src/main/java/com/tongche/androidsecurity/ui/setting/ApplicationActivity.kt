package com.tongche.androidsecurity.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.service.*
import com.tongche.androidsecurity.ui.permission.ApplicationFragment

class ApplicationActivity : FragmentActivity(), ApplicationChangesListener,
    ThemeChangesListener {
    private var mPackageName: String? = null
    private var mInvalidate = false
    var EXTRA_PACKAGE_NAME = "packageName"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        PreferencesService.addThemeListener(this)
        setTheme(PreferencesService.themeId)
        setContentView(R.layout.application_activity)
        val fm: FragmentManager = supportFragmentManager
        val applicationFragment =
            fm.findFragmentById(R.id.fragment_application_details) as ApplicationFragment
        val intent: Intent = intent
        mPackageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        applicationFragment.updateApplication(this, mPackageName!!)
        ApplicationChangesService.registerListener(this)
    }

    /**
     * {@inheritDoc }
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * {@inheritDoc }
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_help -> {
                help()
                return true
            }
            R.id.menu_credits -> {
                credits()
                return true
            }
            R.id.menu_preferences -> {
                preferences()
                return true
            }
        }
        return false
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (mInvalidate && !PermissionService.exists(this, mPackageName)) {
            finish()
        }
    }

    override fun onChangeTheme() {
        recreate()
    }

    private fun help() {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    private fun credits() {
        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }

    private fun preferences() {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    override fun onApplicationChange() {
        mInvalidate = true
    }
}
