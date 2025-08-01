package com.gabriel.pocketstatement.ui.screen.confirmation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.gabriel.pocketstatement.R
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.ui.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    sharedViewModel: SharedViewModel,
    onNavigateBack: () -> Unit,
    onSaveComplete: () -> Unit,
    viewModel: ConfirmationViewModel = hiltViewModel()
) {
    val capturedImageUri by sharedViewModel.capturedImageUri.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ConfirmationViewModel.UiEvent.SaveSuccess -> {
                    sharedViewModel.clearUri()
                    onSaveComplete()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Recibo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        if (state.processedReceipt == null) {
                            viewModel.onAnalyzeClick(capturedImageUri)
                        } else {
                            viewModel.onSaveClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    enabled = !state.isLoading
                ) {
                    val buttonText = if (state.processedReceipt == null) {
                        stringResource(R.string.analyze_receipt_button)
                    } else {
                        stringResource(R.string.save_receipt_button)
                    }
                    Text(text = buttonText)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (capturedImageUri != null) {
                AsyncImage(
                    model = capturedImageUri,
                    contentDescription = "Recibo Capturado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Analisando com IA...")
                    }
                }
            }

            state.processedReceipt?.let { receipt ->
                ProcessedReceiptDetails(receipt = receipt, modifier = Modifier.weight(1f))
            }

            if (state.processedReceipt == null && !state.isLoading) {
                state.error?.let { error ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Erro: $error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProcessedReceiptDetails(receipt: Receipt, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Pré-visualização do Recibo", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
        item { DetailRow("Loja:", receipt.storeName) }
        item { DetailRow("Valor Total:", "R$ ${"%.2f".format(receipt.totalAmount)}") }
        item {
            receipt.transactionDate?.let {
                DetailRow(
                    "Data:",
                    it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }
        }
        item { DetailRow("Categoria:", receipt.category) }

        if (receipt.items.isNotEmpty()) {
            item {
                Text(
                    "Itens:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            items(receipt.items) { item ->
                Text("- ${item.description} (Qtd: ${item.quantity}, Preço: R$ ${"%.2f".format(item.price)})")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
        Text(value)
    }
}