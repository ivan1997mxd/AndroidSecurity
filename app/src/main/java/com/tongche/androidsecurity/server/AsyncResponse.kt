package com.tongche.androidsecurity.server

interface AsyncResponse {
    fun onDataReceivedSuccess(result: String?)
    fun onDataReceivedFailed()
}