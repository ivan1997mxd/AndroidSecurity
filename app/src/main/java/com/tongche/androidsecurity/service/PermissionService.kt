package com.tongche.androidsecurity.service


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.*
import android.util.Log
import com.tongche.androidsecurity.model.AppInfo
import com.tongche.androidsecurity.model.Permission
import com.tongche.androidsecurity.model.PermissionGroup
import com.tongche.androidsecurity.service.Constants
import java.util.*


/**
 * Permission Service
 *
 * @author Pierre Levy
 */
object PermissionService {
    private val mNameComparator = NameComparator()
    private val mScoreComparator = ScoreComparator()
    private var mTrustedApps: MutableList<String?>? = null
    private val mListPermissions: MutableList<String?> = ArrayList()

    /**
     * Get applications sorted by name
     *
     * @param context The context
     * @param sortOrder Sort Order
     * @param showTrusted display trusted
     * @param filter The permission filter
     * @return The list
     */
    fun getApplicationsSortedByName(
        context: Context,
        sortOrder: Boolean,
        showTrusted: Boolean,
        filter: String?
    ): List<AppInfo> {
        var list: List<AppInfo> = getApplications(context, showTrusted)
        Collections.sort(list, mNameComparator)
        if (sortOrder) {
            Collections.reverse(list)
        }
        if (filter != null) {
            list = getFilteredApps(context, list, filter)
        }
        return list
    }

    /**
     * Gets applications list sorted by score
     *
     * @param context The context
     * @param sortOrder Sort Order
     * @param showTrusted display trusted
     * @param filter The permission filter
     * @return The list
     */
    fun getApplicationsSortedByScore(
        context: Context,
        sortOrder: Boolean,
        showTrusted: Boolean,
        filter: String?
    ): List<AppInfo> {
        var list: List<AppInfo> = getApplications(context, showTrusted)
        Collections.sort(list, mScoreComparator)
        if (sortOrder) {
            Collections.reverse(list)
        }
        if (filter != null) {
            list = getFilteredApps(context, list, filter)
        }
        return list
    }

    /**
     * Gets the permission group list
     *
     * @param permissions Permissions
     * @param pm the package manager
     * @return The permission group list
     */
    @SuppressLint("WrongConstant")
    fun getPermissions(permissions: Array<String>?, pm: PackageManager): List<PermissionGroup?> {
        val listGroups: MutableList<PermissionGroup?> = ArrayList<PermissionGroup?>()
        if (permissions != null) {
            for (permission in permissions) {
                try {
                    val pi = pm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS)
                    var group: PermissionGroup? = null
                    if (pi.group != null) {
                        val pgi =
                            pm.getPermissionGroupInfo(pi.group, PackageManager.GET_PERMISSIONS)
                        group = getGroup(listGroups, pi.group)
                        if (group == null) {
                            group = PermissionGroup()
                            group.name = pgi.name
                            group.label = pgi.loadLabel(pm).toString()
                            val description = pgi.loadDescription(pm)
                            val strDescription = description?.toString() ?: "N/A"
                            group.description = strDescription
                            listGroups.add(group)
                        }
                    }
                    val p = Permission()
                    p.name = pi.name
                    p.label = pi.loadLabel(pm).toString()
                    if (pi.loadDescription(pm) != null) {
                        p.description = pi.loadDescription(pm).toString()
                    } else {
                        p.description = "Description non available"
                    }
                    p.isDangerous = pi.protectionLevel != PermissionInfo.PROTECTION_NORMAL
                    group?.addPermission(p)
                } catch (ex: PackageManager.NameNotFoundException) {
                    Log.e(Constants.LOG_TAG, "Permission name not found : $permission")
                }
            }
        }
        return listGroups
    }

    /**
     * Checks if package exists
     *
     * @param context The context
     * @param packageName The package name
     * @return true if the package exists, otherwise false
     */
    fun exists(context: Context, packageName: String?): Boolean {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName!!, PackageManager.GET_PERMISSIONS)
        } catch (ex: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    /**
     * Add a trusted app
     *
     * @param context The context
     * @param appPackage The app package name
     */
    fun addTrustedApp(context: Context, appPackage: String?) {
        val trustedApps = getTrustedApps(context)
        if (!trustedApps!!.contains(appPackage)) {
            trustedApps.add(appPackage)
            saveTrustedApps(context, trustedApps)
        }
    }

    /**
     * Remove the app as trusted
     *
     * @param context The context
     * @param appPackage The app package name
     */
    fun removeTrustedApp(context: Context, appPackage: String?) {
        val trustedApps = getTrustedApps(context)
        if (trustedApps!!.contains(appPackage)) {
            trustedApps.remove(appPackage)
            saveTrustedApps(context, trustedApps)
        }
    }

    /**
     * Checks if the app is trusted
     *
     * @param context The context
     * @param appPackage The app package name
     * @return true if the app is marked as trusted
     */
    fun isTrusted(context: Context, appPackage: String?): Boolean {
        val trustedApps: List<String?>? = getTrustedApps(context)
        return trustedApps!!.contains(appPackage)
    }

    val permissions: Array<String?>
        get() {
            val list: List<String> = ArrayList<String>(mListPermissions)
            Collections.sort(list)
            return list.toTypedArray()
        }

    /**
     * Gets applications
     *
     * @param context The context
     * @param showTrusted Show trusted apps
     * @return The applications list
     */
    private fun getApplications(context: Context, showTrusted: Boolean): List<AppInfo> {
        mListPermissions.clear()
        val list: MutableList<AppInfo> = ArrayList<AppInfo>()
        val pm = context.packageManager
        for (info in pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)) {
            if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val app = AppInfo()
                app.name = info.applicationInfo.loadLabel(pm).toString()
                app.packageName = info.packageName
                app.version = info.versionName
                app.icon = info.applicationInfo.loadIcon(pm)
                app.score = getScore(info.packageName, pm)
                list.add(app)
                registerPermissionApp(app, info.packageName, pm)
            }
        }
        return filterApplications(context, list, showTrusted)
    }

    /**
     * Filter the application list for trusted apps
     *
     * @param context The context
     * @param list The source list
     * @param showTrusted Show trusted
     * @return The filtered list
     */
    private fun filterApplications(
        context: Context,
        list: List<AppInfo>,
        showTrusted: Boolean
    ): List<AppInfo> {
        val listFiltered: MutableList<AppInfo> = ArrayList<AppInfo>()
        val trustedApps: List<String?>? = getTrustedApps(context)
        for (app in list) {
            if (!trustedApps!!.contains(app.packageName)) {
                listFiltered.add(app)
            } else {
                if (showTrusted) {
                    app.isTrusted = true
                    listFiltered.add(app)
                }
            }
        }
        return listFiltered
    }

    /**
     * Gets trusted apps
     *
     * @param context The context
     * @return The list
     */
    private fun getTrustedApps(context: Context): MutableList<String?>? {
        if (mTrustedApps == null) {
            mTrustedApps = loadTrustedPackageList(context)
        }
        return mTrustedApps
    }

    /**
     * Load trusted apps from the preferences
     *
     * @param context The context
     * @return The list
     */
    private fun loadTrustedPackageList(context: Context): MutableList<String?> {
        val trustedPackages: MutableList<String?> = ArrayList()
        val prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val count = prefs.getInt(Constants.KEY_TRUSTED_COUNT, 0)
        for (i in 0 until count) {
            val trusted = prefs.getString(Constants.KEY_TRUSTED_APP + i, "")
            trustedPackages.add(trusted)
        }
        log("loadTrustedPackageList : ", trustedPackages)
        return trustedPackages
    }

    /**
     * Save trusted apps in the preferences
     *
     * @param context The context
     * @param trustedApps The trusted apps list
     */
    private fun saveTrustedApps(context: Context, trustedApps: MutableList<String?>?) {
        val prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(Constants.KEY_TRUSTED_COUNT, trustedApps!!.size)
        for (i in trustedApps.indices) {
            editor.putString(Constants.KEY_TRUSTED_APP + i, trustedApps[i])
        }
        editor.apply()
        mTrustedApps = trustedApps
        log("saveTrustedApps : ", trustedApps)
    }

    /**
     * Score calculation
     *
     * @param packageName The app package name
     * @param pm The package manager
     * @return The score
     */
    @SuppressLint("WrongConstant")
    private fun getScore(packageName: String, pm: PackageManager): Int {
        var score = 0
        try {
            val pinfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            if (pinfo.requestedPermissions != null) {
                for (permission in pinfo.requestedPermissions) {
                    try {
                        val pi = pm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS)
                        score += if (pi.protectionLevel != PermissionInfo.PROTECTION_NORMAL) 100 else 1
                    } catch (ex: PackageManager.NameNotFoundException) {
                        Log.e(
                            Constants.LOG_TAG,
                            "Permission name not found : $permission"
                        )
                    }
                }
            }
        } catch (ex: PackageManager.NameNotFoundException) {
            Log.e(Constants.LOG_TAG, "Error getting package info : $packageName")
        }
        return score
    }

    /**
     * Gets the group
     *
     * @param list The group list
     * @param name The group name
     * @return The Group
     */
    private fun getGroup(list: List<PermissionGroup?>, name: String?): PermissionGroup? {
        for (group in list) {
            if (group!!.name.equals(name)) {
                return group
            }
        }
        return null
    }

    /**
     * Log formatter
     *
     * @param text The message
     * @param trustedPackages The list of trusted app packages
     */
    private fun log(text: String, trustedPackages: List<String?>?) {
        for (trusted in trustedPackages!!) {
            Log.d(Constants.LOG_TAG, text + trusted)
        }
    }

    @SuppressLint("WrongConstant")
    private fun registerPermissionApp(app: AppInfo, packageName: String, pm: PackageManager) {
        try {
            val pinfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            if (pinfo.requestedPermissions != null) {
                for (permission in pinfo.requestedPermissions) {
                    try {
                        val pi = pm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS)
                        var label = pi.loadLabel(pm).toString()
                        if (label != null && label.isNotEmpty()) {
                            label = label.substring(0, 1).toUpperCase() + label.substring(1)
                            register(label)
                        }
                    } catch (ex: PackageManager.NameNotFoundException) {
                        Log.e(
                            Constants.LOG_TAG,
                            "Permission name not found : $permission"
                        )
                    }
                }
            }
        } catch (ex: PackageManager.NameNotFoundException) {
            Log.e(Constants.LOG_TAG, "Error getting package info : $packageName")
        }
    }

    private fun register(permission: String) {
        if (!mListPermissions.contains(permission)) {
            mListPermissions.add(permission)
        }
    }

    private fun getFilteredApps(
        context: Context,
        list: List<AppInfo?>,
        filter: String
    ): List<AppInfo> {
        val listFiltered: MutableList<AppInfo> = ArrayList<AppInfo>()
        for (app in list) {
            if (isAppRequirePermission(context, app!!, filter)) {
                listFiltered.add(app)
            }
        }
        return listFiltered
    }

    @SuppressLint("WrongConstant")
    private fun isAppRequirePermission(context: Context, app: AppInfo, filter: String): Boolean {
        try {
            val pm = context.packageManager
            val pinfo: PackageInfo =
                pm.getPackageInfo(app.packageName!!, PackageManager.GET_PERMISSIONS)
            if (pinfo.requestedPermissions != null) {
                for (permission in pinfo.requestedPermissions) {
                    try {
                        val pi = pm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS)
                        var label = pi.loadLabel(pm).toString()
                        if (label.length > 0) {
                            label = label.substring(0, 1).toUpperCase() + label.substring(1)
                            if (label == filter) {
                                return true
                            }
                        }
                    } catch (ex: PackageManager.NameNotFoundException) {
                        Log.e(
                            Constants.LOG_TAG,
                            "Permission name not found : $permission"
                        )
                    }
                }
            }
        } catch (ex: PackageManager.NameNotFoundException) {
            Log.e(Constants.LOG_TAG, "Error getting package info : " + app.packageName)
        }
        return false
    }

    /**
     * Name comparator
     */
    private class NameComparator : Comparator<AppInfo?> {
        override fun compare(app1: AppInfo?, app2: AppInfo?): Int {
            return app1!!.name!!.compareTo(app2!!.name!!)
        }
    }

    /**
     * Score comparator
     */
    private class ScoreComparator : Comparator<AppInfo?> {
        override fun compare(app1: AppInfo?, app2: AppInfo?): Int {
            return app1!!.score - app2!!.score
        }
    }
}
