/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WalletEntity::class], version = 1)
@TypeConverters(WalletConverter::class)
abstract class DcDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANCE: DcDatabase? = null

        fun getDatabase(
            context: Context
        ): DcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    DcDatabase::class.java,
                    "dc_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DigitalCredentialDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DigitalCredentialDatabaseCallback : Callback()
    }
}