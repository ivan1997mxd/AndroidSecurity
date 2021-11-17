package com.tongche.androidsecurity.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.databinding.FragmentHomeBinding
import com.tongche.androidsecurity.databinding.FragmentNotificationsBinding
import com.tongche.androidsecurity.model.AppInfo
import com.tongche.androidsecurity.model.Report
import com.tongche.androidsecurity.service.ApplicationChangesListener
import com.tongche.androidsecurity.service.ApplicationChangesService
import com.tongche.androidsecurity.service.PermissionService
import com.tongche.androidsecurity.ui.notifications.NotificationsViewModel
import com.tongche.androidsecurity.ui.permission.ApplicationAdapter
import com.tongche.androidsecurity.ui.permission.ApplicationFragment
import com.tongche.androidsecurity.ui.permission.PermissionActivity
import com.tongche.androidsecurity.ui.setting.ApplicationActivity
import com.tongche.androidsecurity.ui.setting.CreditsActivity
import com.tongche.androidsecurity.ui.setting.HelpActivity
import com.tongche.androidsecurity.ui.setting.PreferencesActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//, View.OnClickListener, ApplicationChangesListener
class HomeFragment : Fragment() {

    private val mToggleName = true
    private val mToggleScore = false
    private val mShowTrusted = false
    private val mFilterEnabled = false
    private val mFilterValue: String? = null
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var appArrayList: List<AppInfo>
    private val binding get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        appArrayList = PermissionService.getApplicationsSortedByScore(
            requireContext(),
            mToggleScore,
            mShowTrusted,
            mFilterValue
        )
        binding.listview1.isClickable = true
        binding.listview1.adapter = ApplicationAdapter(context as Activity, appArrayList)
        binding.listview1.setOnItemClickListener { parent, view, position, id ->
            val currentApp = appArrayList[position]
            val result = Bundle()
            result.putString("packageName", currentApp.packageName!!)
            val appDetails = ApplicationFragment()
            appDetails.arguments = result
            val fm: FragmentManager = parentFragmentManager
            Navigation.findNavController(view).navigate(R.id.permission_details,result)
//            fm.beginTransaction().replace(R.id.permission_details, appDetails)


        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//
//    public fun onAppSelected(packageName: String) {
//        mPackageName = packageName
//        val fm: FragmentManager = childFragmentManager
//        val applicationFragment =
//            fm.findFragmentById(R.id.fragment_application_details) as ApplicationFragment?
//        if (applicationFragment != null) {
//            applicationFragment.updateApplication(requireActivity(), packageName)
//        } else {
//            val intent = Intent(activity, ApplicationActivity::class.java)
//            intent.putExtra(EXTRA_PACKAGE_NAME, packageName)
//            startActivity(intent)
//        }
//    }
//
//    override fun onApplicationChange() {
//        refreshAppList()
//        mInvalidate = true
//    }
//
//    override fun onClick(p0: View?) {
//        if (view === mButtonSortByName) {
//            sortByName()
//        } else if (view === mButtonSortByScore) {
//            sortByScore()
//        } else if (view === mButtonShowTrusted) {
//            toggleShowTrusted()
//        } else if (view === mButtonFilter) {
//            toggleFilter()
//        } else if (view === mLayoutFilter) {
//            updateFilter()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_help -> {
//                help()
//                return true
//            }
//            R.id.menu_credits -> {
//                credits()
//                return true
//            }
//            R.id.menu_preferences -> {
//                preferences()
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun onPause() {
//        super.onPause()
//        val prefs: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putInt(KEY_SORT, mSort)
//        editor.putBoolean(KEY_TOGGLE_NAME, mToggleName)
//        editor.putBoolean(KEY_TOGGLE_SCORE, mToggleScore)
//        editor.putBoolean(KEY_SHOW_TRUSTED, mShowTrusted)
//        editor.putBoolean(KEY_FILTER_ENABLED, mFilterEnabled)
//        editor.putString(KEY_FILTER_VALUE, mFilterValue)
//        editor.apply()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val prefs: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
//        mSort = prefs.getInt(KEY_SORT, SORT_SCORE)
//        mToggleName = prefs.getBoolean(KEY_TOGGLE_NAME, false)
//        mToggleScore = prefs.getBoolean(KEY_TOGGLE_SCORE, false)
//        mShowTrusted = prefs.getBoolean(KEY_SHOW_TRUSTED, true)
//        mFilterEnabled = prefs.getBoolean(KEY_FILTER_ENABLED, false)
//        mFilterValue = prefs.getString(KEY_FILTER_VALUE, null)
//        if (mInvalidate) {
//            updateUI()
//            refreshApplicationFragment()
//            mInvalidate = false
//        }
//        refreshIndicators()
//    }
//
//    private fun refreshAppList() {
////        LoadingTask(this).execute()
//        val mList: List<AppInfo> = getApplicationsList()
//        update(mList)
//    }
//
//    private fun refreshApplicationFragment() {
//        val fm: FragmentManager = childFragmentManager
//        val applicationFragment =
//            fm.findFragmentById(R.id.fragment_application_details) as ApplicationFragment?
//        applicationFragment?.updateApplication(requireActivity(), mPackageName!!)
//    }
//
//    private fun help() {
//        val intent = Intent(activity, HelpActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun credits() {
//        val intent = Intent(activity, CreditsActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun preferences() {
//        val intent = Intent(activity, PreferencesActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun sortByName() {
//        mToggleName = !mToggleName
//        mSort = SORT_NAME
//        updateUI()
//    }
//
//    private fun sortByScore() {
//        mToggleScore = !mToggleScore
//        mSort = SORT_SCORE
//        updateUI()
//    }
//
//    private fun toggleShowTrusted() {
//        mShowTrusted = !mShowTrusted
//        updateUI()
//    }
//
//    private fun updateUI() {
//        refreshAppList()
//        refreshIndicators()
//    }
//
//    fun update(list: List<AppInfo>) {
//        mApplicationsListFragment!!.update(list)
//    }
//
//    fun getApplicationsList(): List<AppInfo> {
//        return when (mSort) {
//            SORT_NAME -> PermissionService.getApplicationsSortedByName(
//                requireContext(),
//                mToggleName,
//                mShowTrusted,
//                mFilterValue
//            )
//            SORT_SCORE -> PermissionService.getApplicationsSortedByScore(
//                requireContext(),
//                mToggleScore,
//                mShowTrusted,
//                mFilterValue
//            )
//            else -> PermissionService.getApplicationsSortedByScore(
//                requireContext(),
//                mToggleScore,
//                mShowTrusted,
//                mFilterValue
//            )
//        }
//    }
//
//    private fun refreshIndicators() {
//        if (mSort == SORT_SCORE) {
//            mIndicatorScore!!.setBackgroundResource(R.drawable.bar_on)
//            mIndicatorName!!.setBackgroundResource(R.drawable.bar_off)
//        } else {
//            mIndicatorScore!!.setBackgroundResource(R.drawable.bar_off)
//            mIndicatorName!!.setBackgroundResource(R.drawable.bar_on)
//        }
//        if (mShowTrusted) {
//            mIndicatorTrusted!!.setBackgroundResource(R.drawable.bar_on)
//        } else {
//            mIndicatorTrusted!!.setBackgroundResource(R.drawable.bar_off)
//        }
//        if (mFilterEnabled) {
//            mLayoutFilter!!.visibility = View.VISIBLE
//            mIndicatorFilter!!.setBackgroundResource(R.drawable.bar_on)
//            mTvFilter!!.text = mFilterValue
//        } else {
//            mLayoutFilter!!.visibility = View.GONE
//            mIndicatorFilter!!.setBackgroundResource(R.drawable.bar_off)
//        }
//    }
//
//    private fun toggleFilter() {
//        mFilterEnabled = !mFilterEnabled
//        if (mFilterEnabled) {
//            updateFilter()
//        } else {
//            mFilterValue = null
//            updateUI()
//        }
//    }
//
//    private fun updateFilter() {
//        val items: Array<String?> = PermissionService.permissions
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle(getString(R.string.dialog_title_select_permission))
//        builder.setItems(items) { dialog, item ->
//            mFilterValue = items[item]
//            updateUI()
//        }
//        val alert = builder.create()
//        alert.show()
//    }

}
//
//private class LoadingTask(fragment: HomeFragment) : AsyncTask<Void, Void, Void>() {
//    private var homefragment: HomeFragment = fragment
//    private lateinit var mList: List<AppInfo>
//    override fun doInBackground(vararg p0: Void?): Void? {
//
//        return null
//    }
//
//    override fun onPostExecute(result: Void?) {
//
//    }
//
//}