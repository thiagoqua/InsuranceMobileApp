package com.thiago.online.insurancesapp.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.thiago.online.insurancesapp.data.models.Company
import com.thiago.online.insurancesapp.data.models.Producer

@Composable
fun CompanyFilter(
    companies: List<Company>?,
    onSelected: (String) -> Unit
) {
    val selected = remember { mutableStateOf("Compañía") }
    val expanded = remember { mutableStateOf(false) }

    if(companies != null) {
        val options: MutableList<String> = companies
            .map({ company -> company.name })
            .toMutableList();

        options.add(0,"Compañía");

        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected.value,
                    modifier = Modifier.clickable(onClick = { expanded.value = true }),
                    color = if(selected.value != options[0]) MaterialTheme.colorScheme.primary
                            else Color.Black
                );
                IconButton(onClick = { expanded.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = "show_filters"
                    );
                }
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                options.forEach({ name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selected.value = name;
                            expanded.value = false;
                            onSelected(name);
                        }
                    );
                });
            }
        }
    }
}

@Composable
fun ProducerFilter(
    producers: List<Producer>?,
    onSelected: (String) -> Unit
) {
    val selected = remember { mutableStateOf("Productor") }
    val expanded = remember { mutableStateOf(false) }

    if(producers != null) {
        val options: MutableList<String> = producers
            .map({ producer -> "${producer.firstname} ${producer.lastname}" })
            .toMutableList();

        options.add(0,"Productor");

        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected.value,
                    modifier = Modifier.clickable(onClick = { expanded.value = true }),
                    color = if(selected.value != options[0]) MaterialTheme.colorScheme.primary
                            else Color.Black
                );
                IconButton(onClick = { expanded.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = "show_filters"
                    );
                }
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                options.forEach({ name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selected.value = name;
                            expanded.value = false;
                            onSelected(name);
                        }
                    );
                });
            }
        }
    }
}

@Composable
fun StatusFilter(onSelected: (String) -> Unit) {
    val selected = remember { mutableStateOf("Estado") }
    val expanded = remember { mutableStateOf(false) }
    val options:List<String> = listOf(
        "Estado","ANULADA","EN JUICIO","ACTIVA"
    );

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected.value,
                modifier = Modifier.clickable(onClick = { expanded.value = true }),
                color = if(selected.value != options[0]) MaterialTheme.colorScheme.primary
                        else Color.Black
            );
            IconButton(onClick = { expanded.value = true }) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "show_filters"
                );
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach({ name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        selected.value = name;
                        expanded.value = false;
                        onSelected(name);
                    }
                );
            });
        }
    }
}