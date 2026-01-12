package com.universidade.project_form.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TelaSplash (
    aoFinalizar: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1800)
        aoFinalizar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6650a4)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = "Logo do app",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Despesa Daily",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

        }
    }

}