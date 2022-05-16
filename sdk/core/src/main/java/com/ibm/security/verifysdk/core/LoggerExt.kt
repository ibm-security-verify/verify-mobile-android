/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import android.util.Log
import org.slf4j.Logger

private const val unsupportedLevelMessage =
    "Log level {} is not supported. See https://developer.android.com/reference/android/util/Log#summary"

/**
 * Creates a log message in the format
 *
 *      Entry class={} method={}
 *
 * @param level  The log level for the message. See https://developer.android.com/reference/android/util/Log#summary
 * for the supported range.
 * @since 3.0.0
 */
fun Logger.entering(level: Int = Log.INFO) {

    val message = "Entry class={} method={}"
    var ste: StackTraceElement = Thread.currentThread().stackTrace[4]

    /*  Handles the case when a `level` parameter is provided and no additional internal function
        call with the default value is required.
     */
    if (ste.methodName.equals("invoke")) {
        ste = Thread.currentThread().stackTrace[3]
    }

    when (level) {
        Log.VERBOSE -> trace(message, ste.className, ste.methodName)
        Log.DEBUG -> debug(message, ste.className, ste.methodName)
        Log.INFO -> info(message, ste.className, ste.methodName)
        Log.WARN -> warn(message, ste.className, ste.methodName)
        Log.ERROR -> error(message, ste.className, ste.methodName)
        Log.ASSERT -> error(message, ste.className, ste.methodName)
        else -> warn(unsupportedLevelMessage, level)
    }
}

/**
 * Creates a log message in the format
 *
 *      Exit class={} method={}
 *
 * @param level  The log level for the message. See https://developer.android.com/reference/android/util/Log#summary
 * for the supported range.
 * @since 3.0.0
 */
fun Logger.exiting(level: Int = Log.INFO) {

    val message = "Exit class={} method={}"
    var ste: StackTraceElement = Thread.currentThread().stackTrace[4]

    /*  Handles the case when a `level` parameter is provided and no additional internal function
        call with the default value is required.
    */
    if (ste.methodName.equals("invoke")) {
        ste = Thread.currentThread().stackTrace[3]
    }

    when (level) {
        Log.VERBOSE -> trace(message, ste.className, ste.methodName)
        Log.DEBUG -> debug(message, ste.className, ste.methodName)
        Log.INFO -> info(message, ste.className, ste.methodName)
        Log.WARN -> warn(message, ste.className, ste.methodName)
        Log.ERROR -> error(message, ste.className, ste.methodName)
        Log.ASSERT -> error(message, ste.className, ste.methodName)
        else -> warn(unsupportedLevelMessage, level)
    }
}

/**
 * Creates a log message with the name and ID of the current thread.
 *
 * See https://developer.android.com/reference/android/util/Log#summary for the supported range
 * of the `level`
 *
 * @param level  The log level for the message.
 * @since 3.0.0
 */
fun Logger.threadInfo(level: Int = Log.INFO) {

    val message =
        String.format("threadName=${Thread.currentThread().name}; threadId=${Thread.currentThread().id};")

    when (level) {
        Log.VERBOSE -> trace(message)
        Log.DEBUG -> debug(message)
        Log.INFO -> info(message)
        Log.WARN -> warn(message)
        Log.ERROR -> error(message)
        Log.ASSERT -> error(message)
        else -> warn(unsupportedLevelMessage, level)
    }
}