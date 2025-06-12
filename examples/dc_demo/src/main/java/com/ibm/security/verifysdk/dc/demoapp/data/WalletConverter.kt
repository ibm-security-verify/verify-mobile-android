/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import androidx.room.TypeConverter
import com.ibm.security.verifysdk.core.serializer.DefaultJson
import com.ibm.security.verifysdk.dc.cloud.Wallet
import kotlinx.serialization.json.Json

class WalletConverter {

    @TypeConverter
    fun toString(wallet: Wallet?): String? {
        return wallet?.let { DefaultJson.encodeToString(it) } // Convert Wallet to JSON String
    }

    @TypeConverter
    fun toWallet(walletJson: String?): Wallet? {
        return walletJson?.let { DefaultJson.decodeFromString<Wallet>(it) } // Convert JSON String back to Wallet
    }
}