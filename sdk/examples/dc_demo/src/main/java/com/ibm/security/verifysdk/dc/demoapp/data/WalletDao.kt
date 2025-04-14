/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM wallets")
    fun getAll(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE rowid = :id")
    suspend fun getEntityById(id: Int): WalletEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(walletEntity: WalletEntity)

    @Update
    suspend fun update(walletEntity: WalletEntity)

    @Delete
    suspend fun delete(walletEntity: WalletEntity)
}