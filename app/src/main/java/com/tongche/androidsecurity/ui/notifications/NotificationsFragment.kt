package com.tongche.androidsecurity.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tongche.androidsecurity.databinding.FragmentNotificationsBinding
import com.tongche.androidsecurity.model.Report
import com.tongche.androidsecurity.model.ReportAdapter

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var taskArryList: ArrayList<Report>
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val name = arrayOf("Task1","Task2","Task3")
        val score = arrayOf("99","91","89")
        val riskRank = arrayOf("No risk","Low risk","Low risk")
        taskArryList = ArrayList()
        for (i in name.indices){
            var taskReport = Report(name[i],score[i],riskRank[i],details="some details here")
            taskArryList.add(taskReport)
        }
        binding.lisview.isClickable = true
        binding.lisview.adapter=ReportAdapter(context as Activity,taskArryList)
        binding.lisview.setOnItemClickListener{parent, view, position, id->
            val name = name[position]
            val score = score[position]
            val riskRank =riskRank[position]
            val i = Intent(context,ReportDetailActivity::class.java)
            i.putExtra("name",name)
            i.putExtra("score",score)
            i.putExtra("riskRank",riskRank)
            startActivity(i)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}