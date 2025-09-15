/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp

import android.app.Application
import com.ibm.security.verifysdk.dc.demoapp.data.DcDatabase
import com.ibm.security.verifysdk.dc.demoapp.data.DcRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DcApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { DcDatabase.getDatabase(this)}
    val repository by lazy { DcRepository(database.walletDao()) }
}