package com.thiago.online.insurancesapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thiago.online.insurancesapp.R

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

@Composable
fun Popup(
    enabled:MutableState<Boolean>,
    title:String,
    mainText:@Composable () -> Unit,
    onConfirm:() -> Unit,
    onDismiss:() -> Unit = { enabled.value = false }
) {
    if(enabled.value){
        AlertDialog(
            onDismissRequest = { enabled.value = false },
            title = { Text(text = title) },
            text = mainText,
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(text = "Aceptar");
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Cancelar");
                }
            }
        );
    }
}