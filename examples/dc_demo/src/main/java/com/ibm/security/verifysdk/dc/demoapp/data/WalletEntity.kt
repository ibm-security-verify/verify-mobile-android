/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ibm.security.verifysdk.dc.cloud.Wallet

@Entity(tableName = "wallets")
data class WalletEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wallet: Wallet
)