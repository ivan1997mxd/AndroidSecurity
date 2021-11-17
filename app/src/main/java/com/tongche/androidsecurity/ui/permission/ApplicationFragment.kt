package com.tongche.androidsecurity.ui.permission


import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.model.PermissionGroup
import com.tongche.androidsecurity.service.ApplicationChangesService
import com.tongche.androidsecurity.service.Constants
import com.tongche.androidsecurity.service.PermissionService
import android.R.attr.defaultValue


class ApplicationFragment : Fragment(), View.OnClickListener {
    private var mName: TextView? = null
    private var mIcon: ImageView? = null
    private var mTvPackageName: TextView? = null
    private var mTvVersion: TextView? = null
    private var mPermissions: ExpandableListView? = null
    private var mApplicationLayout: LinearLayout? = null
    private var mNoPermissionLayout: ScrollView? = null
    private var mButtonOpen: Button? = null
    private var mButtonUninstall: Button? = null
    private var mButtonMarket: Button? = null
    private var mButtonTrusted: Button? = null
    private var mTvMessageNoApplication: TextView? = null
    private var mActivity: Activity? = null
    private var mPackageName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.application_details, container, false)
        mApplicationLayout = v.findViewById<View>(R.id.layout_application) as LinearLayout
        mApplicationLayout!!.visibility = View.GONE
        mName = v.findViewById<View>(R.id.name) as TextView
        mIcon = v.findViewById<View>(R.id.icon) as ImageView
        mTvPackageName = v.findViewById<View>(R.id.package_name) as TextView
        mTvVersion = v.findViewById<View>(R.id.version) as TextView
        mButtonOpen = v.findViewById<View>(R.id.button_open) as Button
        mButtonUninstall = v.findViewById<View>(R.id.button_uninstall) as Button
        mPermissions = v.findViewById<View>(R.id.permissions_list) as ExpandableListView
        mNoPermissionLayout = v.findViewById<View>(R.id.layout_no_permission) as ScrollView
        mButtonMarket = v.findViewById<View>(R.id.button_market) as Button
        mButtonTrusted = v.findViewById<View>(R.id.trusted) as Button
        mButtonOpen!!.setOnClickListener(this)
        mButtonUninstall!!.setOnClickListener(this)
        mButtonMarket!!.setOnClickListener(this)
        mButtonTrusted!!.setOnClickListener(this)
        mTvMessageNoApplication = v.findViewById<View>(R.id.message_no_application) as TextView

        val bundle = this.arguments
        if (bundle != null) {
            val mypackage = bundle.getString("packageName")
            updateApplication(requireActivity(), mypackage!!)
        }
        return v
    }

    fun updateApplication(activity: Activity, packageName: String) {
        if (PermissionService.exists(activity, packageName)) {
            try {
                mActivity = activity
                mPackageName = packageName
                val pm = activity.packageManager
                val pi = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                val info = pi.applicationInfo.loadLabel(pm).toString()
                mName!!.text = info
                mIcon!!.setImageDrawable(pi.applicationInfo.loadIcon(pm))
                mTvPackageName!!.text = packageName
                mTvVersion!!.text = pi.versionName
                val listGroups: List<PermissionGroup> =
                    PermissionService.getPermissions(
                        pi.requestedPermissions,
                        pm
                    ) as List<PermissionGroup>
                val adapter = PermissionExpandableListAdapter(requireActivity(), listGroups)
                mPermissions!!.setAdapter(adapter)
                for (i in listGroups.indices) {
                    mPermissions!!.expandGroup(i)
                }
                if (listGroups.isEmpty()) {
                    mNoPermissionLayout!!.visibility = View.VISIBLE
                    mButtonTrusted!!.visibility = View.GONE
                    mButtonMarket!!.visibility = View.VISIBLE
                } else {
                    mNoPermissionLayout!!.visibility = View.GONE
                    mButtonMarket!!.visibility = View.GONE
                    mButtonTrusted!!.visibility = View.VISIBLE
                    if (PermissionService.isTrusted(mActivity!!, mPackageName)) {
                        mButtonTrusted!!.setBackgroundResource(R.drawable.button_trusted_on)
                    } else {
                        mButtonTrusted!!.setBackgroundResource(R.drawable.button_trusted_off)
                    }
                }
                mTvMessageNoApplication!!.visibility = View.GONE
                mApplicationLayout!!.visibility = View.VISIBLE
            } catch (ex: PackageManager.NameNotFoundException) {
                Log.e(Constants.LOG_TAG, "Package name not found : $packageName")
            }
        } else {
            mTvMessageNoApplication!!.visibility = View.VISIBLE
            mApplicationLayout!!.visibility = View.GONE
        }
    }

    /**
     * {@inheritDoc }
     */
    override fun onClick(view: View) {
        if (view === mButtonOpen) {
            open()
        } else if (view === mButtonUninstall) {
            uninstall()
        } else if (view === mButtonMarket) {
            openMarket()
        } else if (view === mButtonTrusted) {
            trust()
        }
    }

    /**
     * Open the application
     */
    private fun open() {
        val pm = mActivity!!.packageManager
        val intentOpen = pm.getLaunchIntentForPackage(mPackageName!!)
        if (intentOpen != null) {
            intentOpen.addCategory(Intent.CATEGORY_LAUNCHER)
            startActivity(intentOpen)
        } else {
            Toast.makeText(mActivity, getString(R.string.message_error_open), Toast.LENGTH_LONG)
                .show()
        }
    }

    /**
     * Uninstall the application
     */
    private fun uninstall() {
        val uri = "package:$mPackageName"
        val uninstallIntent = Intent(Intent.ACTION_DELETE, Uri.parse(uri))
        startActivity(uninstallIntent)
        ApplicationChangesService.notifyListeners()
    }

    /**
     * Open the market
     */
    private fun openMarket() {
        val uri = "market://details?id=$mPackageName"
        val intentGoToMarket = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intentGoToMarket?.let { startActivity(it) }
            ?: Toast.makeText(
                mActivity,
                getString(R.string.message_error_market),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun trust() {
        val res = mActivity!!.resources
        if (PermissionService.isTrusted(mActivity!!, mPackageName)) {
            PermissionService.removeTrustedApp(mActivity!!, mPackageName)
            mButtonTrusted!!.setBackgroundResource(R.drawable.button_trusted_off)
            Toast.makeText(mActivity, R.string.message_untrust, Toast.LENGTH_SHORT).show()
        } else {
            PermissionService.addTrustedApp(mActivity!!, mPackageName)
            mButtonTrusted!!.setBackgroundResource(R.drawable.button_trusted_on)
            Toast.makeText(mActivity, R.string.message_trust, Toast.LENGTH_SHORT).show()
        }
        ApplicationChangesService.notifyListeners()
    }
}