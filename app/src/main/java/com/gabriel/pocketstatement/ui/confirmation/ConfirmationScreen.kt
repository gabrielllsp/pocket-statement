package com.gabriel.pocketstatement.ui.confirmation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.pocketstatement.R
import com.gabriel.pocketstatement.ui.SharedViewModel

@Composable
fun ConfirmationScreen(
    sharedViewModel: SharedViewModel,
    onNavigateBack: () -> Unit,
    viewModel: ConfirmationViewModel = hiltViewModel()
) {
    val capturedBitmap by sharedViewModel.capturedBitmap.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            Button(
                onClick = { viewModel.onAnalyzeClick(capturedBitmap) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !state.isLoading
            ) {
                Text(text = stringResource(R.string.analyze_receipt_button))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (capturedBitmap != null) {
                Image(
                    bitmap = capturedBitmap!!.asImageBitmap(),
                    contentDescription = "Captured Receipt",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            state.extractedText?.let { text ->
                Text(
                    text = "Texto Extraído (Temporário):\n$text",
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.error?.let { error ->
                Text(
                    text = "Erro: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}