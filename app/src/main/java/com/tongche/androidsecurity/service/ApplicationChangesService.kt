package com.tongche.androidsecurity.service

import java.util.ArrayList


/**
 * Application Changes Service
 * @author Pierre Levy
 */
object ApplicationChangesService {
    private val mListListeners: MutableList<ApplicationChangesListener> = ArrayList()

    /**
     * Register a listener
     * @param listener The listener
     */
    @Synchronized
    fun registerListener(listener: ApplicationChangesListener) {
        if (!mListListeners.contains(listener)) {
            mListListeners.add(listener)
        }
    }

    /**
     * Notify all listeners
     */
    @Synchronized
    fun notifyListeners() {
        for (listener in mListListeners) {
            listener.onApplicationChange()
        }
    }
}
