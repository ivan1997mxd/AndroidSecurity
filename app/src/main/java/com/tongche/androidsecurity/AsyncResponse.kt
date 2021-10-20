package com.tongche.androidsecurity

interface AsyncResponse {
    fun onDataReceivedSuccess(result: String?)
    fun onDataReceivedFailed()
}