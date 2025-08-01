package com.gabriel.pocketstatement.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(value)
    }
}