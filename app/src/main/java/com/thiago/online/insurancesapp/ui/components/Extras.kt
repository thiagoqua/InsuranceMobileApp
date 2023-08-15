package com.thiago.online.insurancesapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorText(errorMessage:String) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.titleSmall,
        color = Color.Red,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black
    );
}

@Composable
fun CircleLoader() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(10.dp)
            .padding(140.dp),
        strokeWidth = 10.dp
    );
}