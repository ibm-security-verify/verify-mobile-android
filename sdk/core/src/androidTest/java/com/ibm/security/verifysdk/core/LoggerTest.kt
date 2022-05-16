/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@SmallTest
@RunWith(AndroidJUnit4::class)
class LoggerTest {

    private lateinit var log: Logger

    @Before
    fun setUp() {
        log = LoggerFactory.getLogger(javaClass)
    }

    @After
    fun tearDown() {
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
            Constants.LOGGER_TEST_SIMPLE_TEST_MESSAGE, null
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
     * the one that is set ({@code LogLevel.INFO}.
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
    fun logThreadInfo_defaultLogLevel_shouldWriteToLogcat() {
        log.threadInfo()
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_defaultLogLevel_shouldWriteToLogcat")
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
    fun logThreadInfo_infoLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.INFO)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_infoLogLevel_shouldWriteToLogcat")
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
    fun logThreadInfo_warnLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.WARN)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_warnLogLevel_shouldWriteToLogcat")
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
    fun logThreadInfo_errorLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.ERROR)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_errorLogLevel_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("E %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> E LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_assertLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.ASSERT)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_errorLogLevel_shouldWriteToLogcat")
                .toString()
        assert(logcatMessage.contains(String.format("E %s:", javaClass.simpleName)))
        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    @Test
    fun logThreadInfo_invalidLogLevel_shouldWriteToLogcat() {
        log.threadInfo(10)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("logThreadInfo_invalidLogLevel_shouldWriteToLogcat")
                .toString()
        assert(
            logcatMessage.contains(
                String.format(
                    "W %s: Log level 10 is not supported",
                    javaClass.simpleName
                )
            )
        )
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> D LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_debugLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.DEBUG)

//        TODO: enable debug logging
//        val logcatMessage =
//            TestHelper.getLogsAfterTestStart("logThreadInfo_debugLogLevel_shouldWriteToLogcat")
//                .toString()
//        assert(logcatMessage.contains(String.format("D %s:", javaClass.simpleName)))
//        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
    }

    /**
     * Expected output:
     *
     *      `<Date/Time< <ThreadIDs> D LoggerTest: threadName=main; threadId=123456;
     *
     */
    @Test
    fun logThreadInfo_verboseLogLevel_shouldWriteToLogcat() {
        log.threadInfo(Log.VERBOSE)

//        TODO: enable debug logging
//        val logcatMessage =
//            TestHelper.getLogsAfterTestStart("logThreadInfo_verboseLogLevel_shouldWriteToLogcat")
//                .toString()
//        assert(logcatMessage.contains(String.format("D %s:", javaClass.simpleName)))
//        assert(logcatMessage.contains(Regex("threadName=[^;]*; threadId=[^0-9]*[0-9]+;")))
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
    fun log_enteringyWithDefaultLevel_shouldWriteToLog() {

        log.entering()
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringyWithDefaultLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringyWithInfoLevel_shouldWriteToLog() {

        log.entering(Log.INFO)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringyWithInfoLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringyWithVerboseLevel_shouldWriteToLog() {

        log.entering(Log.VERBOSE)

//        TODO: enable verbose logging
//        log_entryOrExit_shouldWriteToLog(Constants.LOGGER_ENTRY,
//            "log_enteringyWithVerboseLevel_shouldWriteToLog")
    }

    @Test
    fun log_enteringyWithDebugLevel_shouldWriteToLog() {

        log.entering(Log.DEBUG)

//        TODO: enable Debug logging
//        log_entryOrExit_shouldWriteToLog(Constants.LOGGER_ENTRY,
//            "log_enteringyWithDebugLevel_shouldWriteToLog")
    }

    @Test
    fun log_enteringyWithAssertLevel_shouldWriteToLog() {

        log.entering(Log.ASSERT)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringyWithAssertLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringyWithWarnLevel_shouldWriteToLog() {

        log.entering(Log.WARN)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringyWithWarnLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringyWitErrorLevel_shouldWriteToLog() {

        log.entering(Log.ERROR)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_ENTRY,
            "log_enteringyWitErrorLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_enteringyWithInvalidLevel_shouldWriteToLog() {

        log.entering(10)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_enteringyWithInvalidLevel_shouldWriteToLog")
                .toString()

        assert(
            logcatMessage.contains(
                String.format(
                    "W %s: Log level 10 is not supported",
                    javaClass.simpleName
                )
            )
        )
    }

    @Test
    fun log_exitingyWithDefaultLevel_shouldWriteToLog() {

        log.exiting()
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingyWithDefaultLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingyWithInfoLevel_shouldWriteToLog() {

        log.exiting(Log.INFO)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingyWithInfoLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingyWithVerboseLevel_shouldWriteToLog() {

        log.exiting(Log.VERBOSE)

//        TODO: enable verbose logging
//        log_entryOrExit_shouldWriteToLog(Constants.LOGGER_EXIT,
//            "log_exitingyWithVerboseLevel_shouldWriteToLog")
    }

    @Test
    fun log_exitingyWithDebugLevel_shouldWriteToLog() {

        log.exiting(Log.DEBUG)

//        TODO: enable verbose logging
//        log_entryOrExit_shouldWriteToLog(Constants.LOGGER_EXIT,
//            "log_exitingyWithDebugLevel_shouldWriteToLog")
    }

    @Test
    fun log_exitingyWithAssertLevel_shouldWriteToLog() {

        log.exiting(Log.ASSERT)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingyWithAssertLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingyWithWarnLevel_shouldWriteToLog() {

        log.exiting(Log.WARN)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingyWithWarnLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingyWithErrorLevel_shouldWriteToLog() {

        log.exiting(Log.ERROR)
        log_entryOrExit_shouldWriteToLog(
            Constants.LOGGER_EXIT,
            "log_exitingyWithErrorLevel_shouldWriteToLog"
        )
    }

    @Test
    fun log_exitingyWithInvalidLevel_shouldWriteToLog() {

        log.exiting(10)
        val logcatMessage =
            TestHelper.getLogsAfterTestStart("log_exitingyWithInvalidLevel_shouldWriteToLog")
                .toString()

        assert(
            logcatMessage.contains(
                String.format(
                    "W %s: Log level 10 is not supported",
                    javaClass.simpleName
                )
            )
        )
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