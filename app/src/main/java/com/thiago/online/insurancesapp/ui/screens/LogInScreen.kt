package com.thiago.online.insurancesapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thiago.online.insurancesapp.R
import com.thiago.online.insurancesapp.ui.components.ErrorText
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.LoginViewModel

const val LogInScreenName:String = "LOG_IN_SCREEN";

@Composable
fun LogInScreen(
    onSuccess: (String?) -> Unit,       //when the login is successfull invokes it
    checkingUser: Boolean               //if the app is checking in bground if the user is valid
){
    val viewModel:LoginViewModel = hiltViewModel<LoginViewModel>();
    val isLoading:Boolean = viewModel.loading_.observeAsState().value!!;

    BackHandler(enabled = true) {}

    InsurancesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                Center
            ){
                Form(
                    viewModel,
                    isLoading || checkingUser,
                    Modifier.align(Alignment.Center),
                    onSuccess
                );
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(
    viewModel: LoginViewModel,
    isLoading: Boolean,
    modifier: Modifier,
    onSuccess: (String?) -> Unit,
){
    val username:String = viewModel.username_.observeAsState(initial = "").value;
    val password:String = viewModel.password_.observeAsState(initial = "").value;
    val error:String? = viewModel.error_.observeAsState().value;

    val rememberUser = remember { mutableStateOf<Boolean>(false) };

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "company_logo",
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )
        UsernameField(
            username,
            { newValue -> viewModel.usernameChanged(newValue) },
            !isLoading
        );
        Spacer(modifier = Modifier.padding(16.dp));
        PasswordField(
            password,
            { newValue -> viewModel.passwordChanged(newValue) },
            !isLoading
        );
        Spacer(modifier = Modifier.padding(16.dp));
        RememberUserField(
            rememberUser.value,
            { newValue -> rememberUser.value = newValue },
            !isLoading
        );
        Spacer(modifier = Modifier.padding(16.dp));

        if(isLoading){
            LinearProgressIndicator();
        }
        else {
            if(error != null)
                ErrorText(errorMessage = error);
            Button(
                onClick = { viewModel.onLogIn(onSuccess,rememberUser.value) },
                enabled = username.isNotEmpty() && password.isNotEmpty(),
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = MaterialTheme.colorScheme.surfaceVariant
                );
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(
    username: String,
    onValueChange: (String) -> Unit, enabled: Boolean
){
    TextField(
        value = username,
        onValueChange = { text -> onValueChange(text) },
        label = { Text(text = "nombre de usuario") },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.surfaceTint,
            containerColor = MaterialTheme.colorScheme.surface
        ),
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit, enabled: Boolean
){
    val show: MutableState<Boolean> = remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { text -> onValueChange(text) },
        enabled = enabled,
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
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.surfaceTint,
            containerColor = MaterialTheme.colorScheme.surface
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberUserField(
    checked: Boolean,
    onValueChange: (Boolean) -> Unit,
    enabled: Boolean
){
    Row(
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = "Recordarme",
            modifier = Modifier.align(CenterVertically)
        );
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = { newValue -> onValueChange(newValue) },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.tertiary
            )
        );
    }
}