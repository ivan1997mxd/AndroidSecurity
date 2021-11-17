package com.tongche.androidsecurity.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tongche.androidsecurity.databinding.FragmentNotificationsBinding
import com.tongche.androidsecurity.genericvpnpolicy.User
import com.tongche.androidsecurity.model.Report
import com.tongche.androidsecurity.model.ReportAdapter
import com.mongodb.ConnectionString
import com.mongodb.ServerAddress

import com.mongodb.client.MongoClients

import com.mongodb.client.MongoDatabase
import com.tongche.androidsecurity.genericvpnpolicy.NpaDbService
import com.tongche.androidsecurity.genericvpnpolicy.RetrieveDBTask
import com.tongche.androidsecurity.genericvpnpolicy.Supplier
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.io.Serializable
import android.content.Intent.getIntent
import android.widget.ImageView
import com.tongche.androidsecurity.R


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var taskArryList: ArrayList<Report>
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private var npaDbService = NpaDbService label@{
        return@label ConnectionString("mongodb://192.168.1.71:27017")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        taskArryList = npaDbService.read()

//        val name =
//            arrayOf("YouTube", "Facebook", "Twitter", "Netfilx", "Instagram", "WhatsApp", "Amazon")
//        val score = arrayOf("1/70", "2/70", "0/70", "1/70", "2/70", "0/70", "0/70")
//        val riskRank =
//            arrayOf("No risk", "No risk", "No risk", "No risk", "No risk", "No risk", "No risk")
//        taskArryList = ArrayList()
//        for (i in name.indices) {
//            var taskReport = Report(name[i], score[i], riskRank[i], details = "some details here")
//            taskArryList.add(taskReport)
//        }

        binding.listview.isClickable = true
        binding.listview.adapter = ReportAdapter(context as Activity, taskArryList)
        binding.listview.setOnItemClickListener { parent, view, position, id ->
            val currentReport = taskArryList[position]
            val i = Intent(context, ReportDetailActivity::class.java)
            i.putExtra("name", currentReport.name)
            i.putExtra("score", currentReport.score)
            i.putExtra("riskRank", currentReport.riskRank)
            i.putExtra("details", currentReport.details)
            startActivity(i)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}