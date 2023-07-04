package com.thiago.online.insurancesapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun Form(logIn:(String,String) -> Unit){
    val username: MutableState<String> = remember { mutableStateOf("") };
    val password: MutableState<String> = remember { mutableStateOf("") };
    val showPassword: MutableState<Boolean> = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UsernameField(username);
        PasswordField(password,showPassword);
        Button(
            onClick = { logIn(username.value,password.value) },
            enabled = username.value.isNotEmpty() && password.value.isNotEmpty()
        ) {
            Text(text = "Iniciar Sesión")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameField(username:MutableState<String>){
    TextField(
        value = username.value,
        onValueChange = { username.value = it },
        label = { Text(text = "nombre de usuario") },
        modifier = Modifier.padding(15.dp)
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(password:MutableState<String>,show:MutableState<Boolean>){
    TextField(
        value = password.value,
        onValueChange = { password.value = it },
        visualTransformation =
            if(show.value) VisualTransformation.None
            else PasswordVisualTransformation(),
        label = { Text(text = "contraseña") },
        trailingIcon = { IconButton(
            { show.value = !show.value },
            content = { Icon(
                imageVector =   if (!show.value) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff,
                contentDescription = null
            )
            })
        },
        modifier = Modifier.padding(15.dp)
    )
}