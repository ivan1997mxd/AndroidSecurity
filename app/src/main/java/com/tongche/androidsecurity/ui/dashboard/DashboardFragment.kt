package com.tongche.androidsecurity.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var selectedApk: Uri? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    private fun sendSever(){
        if (selectedApk == null){

            return
        }
    }
}