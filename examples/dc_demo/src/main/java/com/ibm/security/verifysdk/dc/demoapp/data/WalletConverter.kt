/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import androidx.room.TypeConverter
import com.ibm.security.verifysdk.dc.Wallet
import kotlinx.serialization.json.Json

class WalletConverter {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun toString(wallet: Wallet?): String? {
        return wallet?.let { json.encodeToString(it) } // Convert Wallet to JSON String
    }

    @TypeConverter
    fun toWallet(walletJson: String?): Wallet? {
        return walletJson?.let { json.decodeFromString<Wallet>(it) } // Convert JSON String back to Wallet
    }
}