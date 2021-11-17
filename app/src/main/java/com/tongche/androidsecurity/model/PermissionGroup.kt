package com.tongche.androidsecurity.model

import java.util.ArrayList

class PermissionGroup {
    /**
     * @return the name
     */
    /**
     * @param name the name to set
     */
    var name: String? = null
    /**
     * @return the label
     */
    /**
     * @param label the label to set
     */
    var label: String? = null
    /**
     * @return the description
     */
    /**
     * @param description the description to set
     */
    var description: String? = null
    private var listPermissions: MutableList<Permission> = ArrayList()

    /**
     * @return the listPermissions
     */
    fun getListPermissions(): List<Permission> {
        return listPermissions
    }

    /**
     * @param listPermissions the listPermissions to set
     */
    fun setListPermissions(listPermissions: MutableList<Permission>) {
        this.listPermissions = listPermissions
    }

    /**
     *
     * @param permission
     */
    fun addPermission(permission: Permission) {
        listPermissions.add(permission)
    }
}
