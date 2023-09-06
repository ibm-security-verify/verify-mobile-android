/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import android.content.Context

object ContextHelper {
    private var applicationContext: Context? = null

    val context
        get() = this.applicationContext
        ?: error("Android context has not been set. Please call init() in your application's onCreate()")

    fun init(context: Context) {
        this.applicationContext = context.applicationContext
    }
}