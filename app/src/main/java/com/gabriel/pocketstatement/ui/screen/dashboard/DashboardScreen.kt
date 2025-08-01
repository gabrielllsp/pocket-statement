package com.gabriel.pocketstatement.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val spendingList by viewModel.spendingState.collectAsStateWithLifecycle()
    val textColor = MaterialTheme.colorScheme.onBackground

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard de Gastos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (spendingList.isEmpty()) {
                Text(
                    "Não há dados de gastos para exibir.\nAdicione alguns recibos primeiro!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    "Gastos por Categoria",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                AndroidView(
                    factory = { context ->
                        PieChart(context).apply {
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            description.isEnabled = false
                            isDrawHoleEnabled = true
                            holeRadius = 25f

                            setUsePercentValues(true)
                            setEntryLabelColor(android.graphics.Color.BLACK)
                            setEntryLabelTextSize(12f)
                            legend.textColor = textColor.toArgb()
                            legend.textSize = 14f
                            legend.isWordWrapEnabled = true // Permite quebrar linha na legenda
                        }
                    },
                    update = { pieChart ->
                        val entries = spendingList.map {
                            PieEntry(it.totalAmount.toFloat(), it.category)
                        }

                        val dataSet = PieDataSet(entries, "").apply {
                            colors =
                                ColorTemplate.MATERIAL_COLORS.toList() + ColorTemplate.VORDIPLOM_COLORS.toList()
                            setValueTextColor(textColor.toArgb())
                            setValueTextSize(14f)
                        }

                        val pieData = PieData(dataSet).apply {
                            setValueFormatter(PercentFormatter(pieChart))
                        }

                        pieChart.data = pieData
                        pieChart.invalidate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )
            }
        }
    }
}