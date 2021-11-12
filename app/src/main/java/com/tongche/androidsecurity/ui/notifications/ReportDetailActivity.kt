package com.tongche.androidsecurity.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.databinding.ActivityReportDetailBinding
import com.tongche.androidsecurity.model.Result


class ReportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        val nameView: TextView = findViewById(R.id.de_name)
        val scoreView: TextView = findViewById(R.id.de_score)
        val riskRankView: TextView = findViewById(R.id.de_rank)
        val detailView: ListView = findViewById(R.id.de_detail)
        val imageView:ImageView = findViewById(R.id.depic)

        val name = intent.getStringExtra("name")
        val score = intent.getStringExtra("score")
        val riskRank = intent.getStringExtra("riskRank")
        val details = intent.getParcelableArrayListExtra<Result>("details")
            ?: throw IllegalStateException("array list is null")
        if (riskRank =="No Risk"){
            imageView.setImageResource(R.drawable.safe)
        }else{
            imageView.setImageResource(R.drawable.unsafe)
        }
        nameView.text = name
        scoreView.text ="Score: "+ score
        riskRankView.text ="Risk rank: "+ riskRank
        detailView.adapter = DetailAdapter(this, details)

    }
}

class DetailAdapter(private val context: Context, private val arrayList: ArrayList<Result>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false)
        val engineName: TextView = view.findViewById(R.id.engineName)
        val category: TextView = view.findViewById(R.id.category)
        val method: TextView = view.findViewById(R.id.method)
        val detail: TextView = view.findViewById(R.id.result)
        //imageView.setImageResource(arrayList[position].imageId)

        engineName.text = arrayList[position].engine_name
        category.text = "Category: "+arrayList[position].category
        method.text = "Method: "+arrayList[position].method
        if(arrayList[position].result=="null"){
            detail.text = "Result: safe"
        }
        else{
            detail.text = "Result: "+arrayList[position].result
        }

        return view
    }
}
