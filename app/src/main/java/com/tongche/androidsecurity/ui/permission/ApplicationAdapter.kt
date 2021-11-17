package com.tongche.androidsecurity.ui.permission

import android.app.Activity
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.model.AppInfo

class ApplicationAdapter(private val mActivity: Activity, list: List<AppInfo>) :
    ListAdapter {
    private val mList: List<AppInfo> = list

    /**
     * {@inheritDoc }
     */
    override fun registerDataSetObserver(arg0: DataSetObserver) {}

    /**
     * {@inheritDoc }
     */
    override fun unregisterDataSetObserver(arg0: DataSetObserver) {}

    /**
     * {@inheritDoc }
     */
    override fun getCount(): Int {
        return mList.size
    }

    /**
     * {@inheritDoc }
     */
    override fun getItem(position: Int): Any {
        return mList[position]
    }

    /**
     * {@inheritDoc }
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * {@inheritDoc }
     */
    override fun hasStableIds(): Boolean {
        return true
    }

    /**
     * {@inheritDoc }
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(mActivity)
        var convertView1: View = inflater.inflate(R.layout.application_list_item, null)
        val info: AppInfo = mList[position]
        if (convertView1 == null) {
            val inflater = mActivity.layoutInflater
            convertView1 = inflater.inflate(R.layout.application_list_item, parent, false)
        }
        val imageView = convertView1.findViewById<View>(R.id.icon) as ImageView
        imageView.setImageDrawable(info.icon)
        val tvName = convertView1.findViewById<View>(R.id.name) as TextView
        tvName.setText(info.name)
        val tvVersion = convertView1.findViewById<View>(R.id.version) as TextView
        tvVersion.setText(info.version)
        val tvScore = convertView1.findViewById<View>(R.id.score) as TextView
        tvScore.text = "Score : " + info.score
        val imageScore = convertView1.findViewById<View>(R.id.score_icon) as ImageView
        if (info.score == 0) {
            imageScore.setImageResource(R.drawable.no_permission)
        } else if (info.score < 5) {
            imageScore.setImageResource(R.drawable.normal)
        } else {
            imageScore.setImageResource(R.drawable.dangerous)
        }
        if (info.isTrusted) {
            imageScore.setImageResource(R.drawable.trusted_row)
        }
        return convertView1
    }

    /**
     * {@inheritDoc }
     */
    override fun getItemViewType(arg0: Int): Int {
        return 0
    }

    /**
     * {@inheritDoc }
     */
    override fun getViewTypeCount(): Int {
        return 1
    }

    /**
     * {@inheritDoc }
     */
    override fun isEmpty(): Boolean {
        return mList.isEmpty()
    }

    /**
     * {@inheritDoc }
     */
    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    /**
     * {@inheritDoc }
     */
    override fun isEnabled(arg0: Int): Boolean {
        return true
    }

}
