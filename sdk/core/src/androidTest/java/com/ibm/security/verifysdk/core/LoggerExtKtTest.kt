/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.ibm.security.verifysdk.core.extension.entering
import com.ibm.security.verifysdk.core.extension.exiting
import com.ibm.security.verifysdk.core.extension.threadInfo
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

@MediumTest
@RunWith(AndroidJUnit4::class)
internal class LoggerExtKtTest {

    private lateinit var log: Logger

    @Before
    fun setup() {
        log = LoggerFactory.getLogger(javaClass)
    }


    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> I LoggerTest: Simple test message`
     *
     */
    @Test
    fun log_validMessage_shouldWriteToLogcat() {
        log.info(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessage_shouldWriteToLogcat")
                .toString()
        assert(
            logcatMessage.contains(
                String.format(
                    "I %s: %s",
                    javaClass.simpleName,
                    Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE
                )
            )
        )
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> D LoggerTest: Simple test message A1 B2 C3`
     *
     */
    @Test
    fun log_validMessageWithVarArgs_shouldWriteToLogcat() {
        log.info(
            Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE_WITH_ARGS,
            Constants.LOGGER_TEST_A1, Constants.LOGGER_TEST_B2, Constants.LOGGER_TEST_C3
        )
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessageWithVarArgs_shouldWriteToLogcat")
                .toString()
        assert(
            logcatMessage.contains(
                String.format(
                    "I %s: %s %s %s %s",
                    javaClass.simpleName,
                    Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE,
                    Constants.LOGGER_TEST_A1, Constants.LOGGER_TEST_B2, Constants.LOGGER_TEST_C3
                )
            )
        )
    }

    /**
     * `varargs` can be `null if it is not required, because the message does not contain place
     * holders.
     *
     */
    @Test
    fun log_validMessageArgsNotNeeded_shouldWriteToLogcat() {
        log.warn(
            /* msg = */ Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE, /* t = */ null
        )
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessageArgsNotNeeded_shouldWriteToLogcat")
                .toString()
        assert(
            logcatMessage.contains(
                String.format(
                    "W %s: %s",
                    javaClass.simpleName,
                    Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE
                )
            )
        )
    }

    /**
     * No message should be logged, because the log level for that message is higher then
     * the one that is set `LogLevel.INFO`.
     */
    @Test
    fun log_logLevelToHigh_shouldNotWriteToLogcat() {
        log.debug(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_logLevelToHigh_shouldNotWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE).not())
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> I LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_default_shouldWriteToLogcat() {
        log.threadInfo()
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_default_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("I %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> I LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_info_shouldWriteToLogcat() {
        log.threadInfo(Level.INFO)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_info_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("I %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> W LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_warn_shouldWriteToLogcat() {
        log.threadInfo(Level.WARN)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_warn_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("W %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> E LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_error_shouldWriteToLogcat() {
        log.threadInfo(Level.ERROR)

        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_error_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("E %s", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    @Test
        (expected = IllegalArgumentException::class)
    fun logThreadInfo_invalid_shouldThrowException() {
        log.threadInfo(Level.valueOf("SUPER"))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> D LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    @Ignore("Configuration required for DEBUG level")
    fun logThreadInfo_debug_shouldWriteToLogcat() {
        log.threadInfo(Level.DEBUG)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_debug_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("D %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> D LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    @Ignore("Configuration required for DEBUG level")
    fun logThreadInfo_trace_shouldWriteToLogcat() {
        log.threadInfo(Level.TRACE)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_trace_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("D %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     *  No message should be logged.
     */
    @Test
    fun log_validMessageButLogLevelInsufficient_shouldNotWriteToLog() {
        log.debug(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        log.trace(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessageButLogLevelInsufficient_shouldNotWriteToLog")
                .toString()
        assert(logcatMessage.contains(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE).not())
    }

    /**
     *  Every message should be logged.
     */
    @Test
    fun log_validMessageAndLogLevelSufficient_shouldWriteToLog() {
        log.info(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        log.warn(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        log.error(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessageAndLogLevelSufficient_shouldWriteToLog")
                .toString()
        assert(logcatMessage.contains(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE))
    }

    /**
     * Logs the message and the exception.
     */
    @Test
    fun log_validMessageWithException_shouldWriteToLog() {
        log.info(
            Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE,
            IllegalArgumentException(Constants.LOGGER_TEST_EXCEPTION_TEXT)
        )
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_validMessageWithException_shouldWriteToLog")
                .toString()
        assert(logcatMessage.contains(Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE))
        assert(logcatMessage.contains(Constants.LOGGER_TEST_EXCEPTION_TEXT))
        assert(logcatMessage.contains(java.lang.IllegalArgumentException::class.java.simpleName))
    }

    @Test
    fun log_enteringWithDefaultLevel_shouldWriteToLog() {
        log.entering()
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWithDefaultLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringWithInfoLevel_shouldWriteToLog() {
        log.entering(Level.INFO)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWithInfoLevel_shouldWriteToLog"
        )
    }

    @Test
    @Ignore("Configuration required for DEBUG level")
    fun log_enteringWithTraceLevel_shouldWriteToLog() {
        log.entering(Level.TRACE)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWithVerboseLevel_shouldWriteToLog"
        )
    }

    @Test
    @Ignore("Configuration required for DEBUG level")
    fun log_enteringWithDebugLevel_shouldWriteToLog() {
        log.entering(Level.DEBUG)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWithDebugLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringWithWarnLevel_shouldWriteToLog() {
        log.entering(Level.WARN)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWithWarnLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringWitErrorLevel_shouldWriteToLog() {
        log.entering(Level.ERROR)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringWitErrorLevel_shouldWriteToLog"
        )
    }

    @Test
        (expected = IllegalArgumentException::class)
    fun log_enteringWithInvalidLevel_shouldThrowException() {
        log.entering(Level.valueOf("SUPER"))
    }

    @Test
    fun log_exitingWithDefaultLevel_shouldWriteToLog() {
        log.exiting()
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithDefaultLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingWithInfoLevel_shouldWriteToLog() {
        log.exiting(Level.INFO)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithInfoLevel_shouldWriteToLog"
        )
    }

    @Test
    @Ignore("Configuration required for DEBUG level")
    fun log_exitingWithTraceLevel_shouldWriteToLog() {
        log.exiting(Level.TRACE)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithVerboseLevel_shouldWriteToLog"
        )
    }

    @Test
    @Ignore("Configuration required for DEBUG level")
    fun log_exitingWithDebugLevel_shouldWriteToLog() {
        log.exiting(Level.DEBUG)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithDebugLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingWithWarnLevel_shouldWriteToLog() {
        log.exiting(Level.WARN)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithWarnLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingWithErrorLevel_shouldWriteToLog() {
        log.exiting(Level.ERROR)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingWithErrorLevel_shouldWriteToLog"
        )
    }

    @Test
        (expected = IllegalArgumentException::class)
    fun log_exitingWithInvalidLevel_shouldThrowException() {
        log.exiting(Level.valueOf("SUPER"))
    }

    private fun log_entryOrExit_shouldWriteToLog(entryOrExit: String, methodName: String) {
        val logcatMessage =
            TestHelper.getLogsAfterTestStart(methodName)
                .toString()

        assert(
            logcatMessage.contains(
                String.format(
                    "%s=%s",
                    Constants.LOGGER_CLASS,
                    javaClass.name
                )
            )
        )
        assert(logcatMessage.contains(String.format("%s=%s", Constants.LOGGER_METHOD, methodName)))
        assert(logcatMessage.contains(entryOrExit))
    }
}