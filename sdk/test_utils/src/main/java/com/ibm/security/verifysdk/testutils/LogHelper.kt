/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.testutils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min

object LogHelper {

    private var log: Logger = LoggerFactory.getLogger(javaClass)

    fun largeLog(tag: String, content: String, count : Int = 1, limit: Int = 4000) {
        val lineLimit = min(limit, 4000)
        if (content.length > lineLimit) {
            log.info("$tag-$count: ${content.substring(0, lineLimit)}")
            largeLog(tag, content.substring(lineLimit), count + 1)
        } else {
            log.info("$tag-$count: $content")
        }
    }
}