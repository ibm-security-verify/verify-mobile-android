/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

open class DcRepository(private val walletDao: WalletDao) {

    val allWallets: Flow<List<WalletEntity>> = walletDao.getAll()

    @WorkerThread
    suspend fun insert(walletEntity: WalletEntity) {
        walletDao.insert(walletEntity)
    }

    @WorkerThread
    suspend fun update(walletEntity: WalletEntity) {
        walletDao.update(walletEntity)
    }

    @WorkerThread
    suspend fun delete(walletEntity: WalletEntity) {
        walletDao.delete(walletEntity)
    }
}