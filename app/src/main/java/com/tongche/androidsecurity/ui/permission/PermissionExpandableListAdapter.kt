package com.tongche.androidsecurity.ui.permission

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.model.Permission
import com.tongche.androidsecurity.model.PermissionGroup

class PermissionExpandableListAdapter(
    private val mActivity: Activity,
    groupsList: List<PermissionGroup>
) :
    BaseExpandableListAdapter() {
    private val mGroupsList: List<PermissionGroup> = groupsList

    /**
     * {@inheritDoc }
     */
    override fun getGroupCount(): Int {
        return mGroupsList.size
    }

    /**
     * {@inheritDoc }
     */
    override fun getChildrenCount(position: Int): Int {
        return mGroupsList[position].getListPermissions().size
    }

    /**
     * {@inheritDoc }
     */
    override fun getGroup(position: Int): Any {
        return mGroupsList[position]
    }

    /**
     * {@inheritDoc }
     */
    override fun getChild(positionGroup: Int, positionChild: Int): Any {
        return mGroupsList[positionGroup].getListPermissions().get(positionChild)
    }

    /**
     * {@inheritDoc }
     */
    override fun getGroupId(position: Int): Long {
        return position.toLong()
    }

    /**
     * {@inheritDoc }
     */
    override fun getChildId(arg0: Int, arg1: Int): Long {
        return (1000 * arg0 + arg1).toLong()
    }

    /**
     * {@inheritDoc }
     */
    override fun hasStableIds(): Boolean {
        return false
    }

    /**
     * {@inheritDoc }
     */
    override fun getGroupView(
        position: Int,
        arg1: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val inflater: LayoutInflater = LayoutInflater.from(mActivity)
        val convertView1: View = inflater.inflate(R.layout.group_list_item, parent, false)
        val group: PermissionGroup = mGroupsList[position]
        val tvLabel = convertView1.findViewById<View>(R.id.group_label) as TextView
        tvLabel.setText(group.label)
        return convertView1
    }

    /**
     * {@inheritDoc }
     */
    override fun getChildView(
        positionGroup: Int,
        positionChild: Int,
        arg2: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val permission: Permission =
            mGroupsList[positionGroup].getListPermissions().get(positionChild)
        val inflater = mActivity.layoutInflater
        val convertView2 = inflater.inflate(R.layout.permission_list_item, parent, false)
        val nRes: Int = if (permission.isDangerous) R.drawable.dangerous else R.drawable.normal
        val imageView = convertView2.findViewById<View>(R.id.icon_danger) as ImageView
        imageView.setImageResource(nRes)
        val tvLabel = convertView2.findViewById<View>(R.id.permission_label) as TextView
        tvLabel.setText(permission.label)
        val tvName = convertView2.findViewById<View>(R.id.permission_name) as TextView
        tvName.setText(permission.name)
        val tvDescription = convertView2.findViewById<View>(R.id.permission_description) as TextView
        tvDescription.setText(permission.description)
        return convertView2
    }

    /**
     * {@inheritDoc }
     */
    override fun isChildSelectable(arg0: Int, arg1: Int): Boolean {
        return true
    }

}
