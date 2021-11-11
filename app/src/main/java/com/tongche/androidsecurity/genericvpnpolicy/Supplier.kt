package com.tongche.androidsecurity.genericvpnpolicy

import com.mongodb.ConnectionString

fun interface Supplier<T> {
    fun get(): T
}
