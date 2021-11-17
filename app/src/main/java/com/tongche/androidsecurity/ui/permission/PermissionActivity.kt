package com.tongche.androidsecurity.ui.permission

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.service.ApplicationChangesListener
import com.tongche.androidsecurity.service.ApplicationChangesService
import com.tongche.androidsecurity.service.PreferencesService
import com.tongche.androidsecurity.service.ThemeChangesListener
//, ApplicationsListFragment.AppListEventsCallback,
//    View.OnClickListener, ApplicationChangesListener, ThemeChangesListener
class PermissionActivity : FragmentActivity(), View.OnClickListener {
    var EXTRA_PACKAGE_NAME = "packageName"
    private val KEY_SORT = "sort"
    private val KEY_TOGGLE_NAME = "toggle_name"
    private val KEY_TOGGLE_SCORE = "toggle_score"
    private val KEY_SHOW_TRUSTED = "show_trusted"
    private val KEY_FILTER_ENABLED = "filter_enabled"
    private val KEY_FILTER_VALUE = "filter_value"

    private val SORT_SCORE = 0
    private val SORT_NAME = 1

    private lateinit var mButtonSortByName: TextView
    private lateinit var mButtonSortByScore: TextView
    private lateinit var mButtonShowTrusted: ImageView
    private lateinit var mButtonFilter: ImageView
    private lateinit var mLayoutFilter: View
    private lateinit var mTvFilter: TextView
    private lateinit var mIndicatorName: View
    private lateinit var mIndicatorScore: View
    private lateinit  var mIndicatorTrusted: View
    private lateinit var mIndicatorFilter: View
    private lateinit var mApplicationsListFragment: ApplicationsListFragment
    private var mToggleName = true
    private var mToggleScore = false
    private var mShowTrusted = false
    private var mFilterEnabled = false
    private lateinit var mFilterValue: String
    private var mSort = 0
    private var mInvalidate = false
    private lateinit var mPackageName: String

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
//        PreferencesService.addThemeListener(this)
//        setTheme(PreferencesService.themeId)

        mButtonSortByName = findViewById(R.id.button_sort_name)
        mButtonSortByName.setOnClickListener(this)

        mButtonSortByScore = findViewById(R.id.button_sort_score)
        mButtonSortByScore.setOnClickListener(this)

        mButtonShowTrusted = findViewById(R.id.button_show_trusted)
        mButtonShowTrusted.setOnClickListener(this)

        mButtonFilter = findViewById(R.id.button_filter)
        mButtonFilter.setOnClickListener(this)

        mTvFilter = findViewById(R.id.label_filter)
        mLayoutFilter = findViewById(R.id.label_filter)
        mLayoutFilter.setOnClickListener(this)

        mIndicatorName = findViewById(R.id.indicator_name)
        mIndicatorScore = findViewById(R.id.indicator_score)
        mIndicatorTrusted = findViewById(R.id.indicator_trusted)
        mIndicatorFilter = findViewById(R.id.indicator_filter)

        val fm: FragmentManager = supportFragmentManager
        mApplicationsListFragment = fm.findFragmentById(R.id.fragment_applications_list) as ApplicationsListFragment

//        ApplicationChangesService.registerListener(this)
        mInvalidate = true
    }

    override fun onClick(view: View?) {
        if (view === mButtonSortByName) {
//            sortByName()
        } else if (view === mButtonSortByScore) {
//            sortByScore()
        } else if (view === mButtonShowTrusted) {
//            toggleShowTrusted()
        } else if (view === mButtonFilter) {
//            toggleFilter()
        } else if (view === mLayoutFilter) {
//            updateFilter()
        }
    }

}

