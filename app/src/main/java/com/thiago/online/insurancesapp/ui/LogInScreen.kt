package com.thiago.online.insurancesapp.ui

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.thiago.online.insurancesapp.viewmodel.LoginViewModel

@Composable
fun LogInScreen(preferences: SharedPreferences){
    val viewModel:LoginViewModel = LoginViewModel(preferences);
    val isLoading:Boolean = viewModel.loading_.observeAsState(initial = false).value;

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ){
        Form(viewModel,isLoading,Modifier.align(Alignment.Center));
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun Form(viewModel: LoginViewModel, isLoading: Boolean,modifier:Modifier){
    val username:String = viewModel.username_.observeAsState(initial = "").value;
    val password:String = viewModel.password_.observeAsState(initial = "").value;
    val loginEnabled:Boolean = viewModel.loginEnabled_.observeAsState(initial = false).value;

    Column(
        modifier = Modifier
    ) {
        UsernameField(username,{ newValue -> viewModel.usernameChanged(newValue)});
        Spacer(modifier = Modifier.padding(16.dp));
        PasswordField(password, { newValue -> viewModel.passwordChanged(newValue)});
        Spacer(modifier = Modifier.padding(16.dp));

        if(isLoading){
            LinearProgressIndicator();
        }
        else {
            Button(
                onClick = { viewModel.onLogIn() },
                enabled = loginEnabled
            ) {
                Text(text = "Iniciar Sesión")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameField(username: String,onValueChange:(String) -> Unit){
    TextField(
        value = username,
        onValueChange = { text -> onValueChange(text) },
        label = { Text(text = "nombre de usuario") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF000000),
        )
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(password: String,onValueChange:(String) -> Unit){
    val show: MutableState<Boolean> = remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { text -> onValueChange(text) },
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1
    )
}