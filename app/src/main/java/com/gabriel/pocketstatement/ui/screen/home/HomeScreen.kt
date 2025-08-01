package com.gabriel.pocketstatement.ui.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.pocketstatement.R
import com.gabriel.pocketstatement.ui.screen.home.components.EmptyStateComponent
import com.gabriel.pocketstatement.ui.screen.home.components.ReceiptListItem
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCamera: () -> Unit,
    onReceiptClick: (Long) -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.ShowUndoSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Recibo deletado",
                        actionLabel = "Desfazer",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onUndoDelete(event.receipt)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onNavigateToDashboard) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = "Ir para o Dashboard"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCamera) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = stringResource(R.string.add_receipt_button)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.receipts.isEmpty() && !state.isLoading) {
                EmptyStateComponent(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.receipts,
                        key = { receipt -> receipt.id }
                    ) { receipt ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.onDeleteReceipt(receipt)
                                    true
                                } else {
                                    false
                                }
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = true,
                            backgroundContent = { SwipeBackground(dismissState) },
                            content = {
                                ReceiptListItem(
                                    receipt = receipt,
                                    modifier = Modifier.clickable { onReceiptClick(receipt.id) }
                                )
                            }
                        )
                    }
                }
            }
            if (state.isLoading && state.receipts.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color.Transparent
        },
        label = "background color"
    )
    val scale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.2f else 0.8f,
        label = "icon scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "Deletar",
            modifier = Modifier.scale(scale),
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}