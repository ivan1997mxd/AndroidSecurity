package com.tongche.androidsecurity.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.databinding.ActivityReportDetailBinding

class ReportDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_report_detail)

        val name = intent.getStringExtra("name")
        val score = intent.getStringExtra("score")
        val riskRank = intent.getStringExtra("riskRank")
        val det = "Here are some details"
        binding.deName.text=name
        binding.deScore.text = score
        binding.deRank.text=riskRank
        binding.detail.text=det
    }
}