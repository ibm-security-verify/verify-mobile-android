/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.logging.LogManager

class MainActivity : AppCompatActivity() {

//    private val log: Logger = LoggerFactory.getLogger(javaClass.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)
        log.atLevel(Level.DEBUG).log("XXX DEBUG2")

        log.debug("XXX DEBUG")
        log.error("XXX ERROR")
        log.warn("XXX WARN")
        log.info("XXX INFO")
        log.trace("XXX TRACE")
        log.trace("XXX TRACE")
    }
}
