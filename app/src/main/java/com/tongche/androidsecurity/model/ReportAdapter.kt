package com.tongche.androidsecurity.model
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.ui.notifications.NotificationsFragment

class ReportAdapter(private val context: Activity, private val arrayList: ArrayList<Report>):ArrayAdapter<Report>
    (context, R.layout.list_item,arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.list_item,null)
        var imageView:ImageView = view.findViewById(R.id.pic)
        var testName: TextView = view.findViewById(R.id.targetName)
        var score: TextView = view.findViewById(R.id.apiResult)
        var rank: TextView = view.findViewById(R.id.riskRank)

        if (arrayList[position].riskRank =="No Risk"){
            imageView.setImageResource(R.drawable.safe)
        }else{
            imageView.setImageResource(R.drawable.unsafe)
        }

        testName.text = arrayList[position].name
        score.text = "Score: "+arrayList[position].score
        rank.text ="Risk rank: " +arrayList[position].riskRank
        return view
    }
}