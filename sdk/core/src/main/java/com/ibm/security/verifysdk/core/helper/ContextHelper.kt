/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.helper

import android.content.Context

object ContextHelper {
    private var applicationContext: Context? = null

    val context
        get() = applicationContext
        ?: error("Android context has not been set. Please call init() in your application's onCreate()")

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}