/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.data.WalletEntity
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow


@Composable
fun WalletDetails(wallet: WalletEntity) {
    Column {
        LabelValueRow(label = "ID", value = wallet.wallet.agent.id,
            valueTextSizeReduction = 1) // to avoid new lines
        Spacer(modifier = Modifier.height(12.dp))
        LabelValueRow(label = "Name", value = wallet.wallet.agent.name)
        Spacer(modifier = Modifier.height(12.dp))
        LabelValueRow(label = "Host", value = wallet.wallet.baseUri.host)

        Spacer(modifier = Modifier.height(24.dp))

        LabelValueRow(label = "Credentials", value = "${wallet.wallet.credentials.size}")
        Spacer(modifier = Modifier.height(6.dp))
        LabelValueRow(label = "Verifications", value = "${wallet.wallet.verifications.size}")
    }
}