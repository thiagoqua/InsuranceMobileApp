package com.thiago.online.insurancesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.thiago.online.insurancesapp.data.models.Company
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.data.models.Producer

@Composable
fun Popup(
    enabled: MutableState<Boolean>,
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

@Composable
fun FilterPopup(
    enabled: MutableState<Boolean>,
    insureds: List<Insured>,
    producers: List<Producer>?,
    companies: List<Company>?,
    onApply: (Array<String?>) -> Unit
) {
    // 0 <- company filter
    // 1 <- producer filter
    // 2 <- life filter
    // 3 <- status filter
    var filters:Array<String?> = arrayOfNulls(4);

    if(enabled.value)
        AlertDialog(
            onDismissRequest = { enabled.value = false; },
            confirmButton = {
                Button(onClick = {
                    enabled.value = false;
                    onApply(filters);
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Aplicar");
                        Spacer(Modifier.size(5.dp));
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "apply_filters"
                        );
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = { enabled.value = false; },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Descartar");
                        Spacer(Modifier.size(5.dp));
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "cancel_filters"
                        );
                    }
                }
            },
            title = { Text("Filtros")},
            text = {
                Column() {
                    CompanyFilter(
                        companies,
                        { selected -> filters[0] = selected }
                    );
                    ProducerFilter(
                        producers,
                        { selected -> filters[1] = selected }
                    );
                    //        LifeFilter({ selected -> filters[2] = selected });
                    StatusFilter({ selected -> filters[3] = selected });
                }
            },
            modifier = Modifier.fillMaxWidth()
        );
}