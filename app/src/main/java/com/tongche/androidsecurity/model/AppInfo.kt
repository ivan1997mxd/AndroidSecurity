package com.tongche.androidsecurity.model

import android.graphics.drawable.Drawable


/**
 * Application Info object
 * @author Pierre Levy
 */
class AppInfo {
    /**
     * @return the name
     */
    /**
     * @param name the name to set
     */
    var name: String? = null
    /**
     * @return the packageName
     */
    /**
     * @param packageName the packageName to set
     */
    var packageName: String? = null
    /**
     * @return the version
     */
    /**
     * @param version the version to set
     */
    var version: String? = null
    /**
     * @return the icon
     */
    /**
     * @param icon the icon to set
     */
    var icon: Drawable? = null
    /**
     * @return the score
     */
    /**
     * @param score the score to set
     */
    var score = 0
    var isTrusted = false
}
