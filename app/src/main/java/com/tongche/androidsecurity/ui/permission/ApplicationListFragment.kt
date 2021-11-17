package com.tongche.androidsecurity.ui.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.model.AppInfo
import com.tongche.androidsecurity.ui.permission.ApplicationAdapter
import com.tongche.androidsecurity.ui.permission.ApplicationFragment
import com.tongche.androidsecurity.ui.setting.ApplicationActivity
import java.lang.ClassCastException

class ApplicationsListFragment() : ListFragment() {
    private var mContainerCallback: AppListEventsCallback? = null
    private var mActivity: Activity? = null
    fun update(listApplications: List<AppInfo>) {
        fillData(listApplications)
    }

    /**
     * Interface
     */
    interface AppListEventsCallback {
        /**
         * Callback
         * @param packageName The package
         */
        fun onAppSelected(packageName: String?)
    }

    /**
     * {@inheritDoc }
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerForContextMenu(listView)
    }

    /**
     * {@inheritDoc }
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            // check that the containing activity implements our callback
            mContainerCallback = activity as AppListEventsCallback
            mActivity = activity
        } catch (e: ClassCastException) {
            activity.finish()
            throw ClassCastException(
                activity.toString()
                        + " must implement AppListEventsCallback"
            )
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val adapter: Adapter = l.adapter
        val app = adapter.getItem(position) as AppInfo
        mContainerCallback!!.onAppSelected(app.packageName)
    }

    private fun fillData(listApps: List<AppInfo>) {
        val activity: Activity? = activity
        if (activity != null) {
            val apps = ApplicationAdapter(activity, listApps)
            listAdapter = apps
        }
    }
}