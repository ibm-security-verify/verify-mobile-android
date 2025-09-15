/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ibm.security.verifysdk.dc.demoapp.data.DcRepository
import com.ibm.security.verifysdk.dc.demoapp.data.WalletEntity
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

class WalletViewModel(private val repository: DcRepository) : ViewModel() {

    private val _allWallets = MutableStateFlow<List<WalletEntity>>(emptyList())
    val allWallets: StateFlow<List<WalletEntity>> = _allWallets

    private val _hostLocal = mutableStateOf("")
    var hostLocal by _hostLocal

    private val _nickname = mutableStateOf("user")
    var nickname by _nickname

    init {
        viewModelScope.launch {
            repository.allWallets.collect { walletList ->
                _allWallets.value = walletList
            }
        }
    }

    fun updateHost(newHost: String) {
        hostLocal = newHost
    }

    fun insert(walletEntity: WalletEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(walletEntity)
    }

    fun update(walletEntity: WalletEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(walletEntity)
    }

    fun delete(walletEntity: WalletEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(walletEntity)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun updateCredentials(walletEntity: WalletEntity, newCredentials: MutableList<CredentialDescriptor>) {
        val updatedWalletEntity = walletEntity.copy(
            wallet = walletEntity.wallet.copy(credentials = newCredentials)
        )
        update(updatedWalletEntity)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun updateVerification(walletEntity: WalletEntity, newVerifications: MutableList<VerificationInfo>) {
        val updatedWalletEntity = walletEntity.copy(
            wallet = walletEntity.wallet.copy(verifications = newVerifications)
        )
        update(updatedWalletEntity)
    }
}

class WalletViewModelFactory(private val repository: DcRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            return WalletViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}