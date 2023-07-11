package com.thiago.online.insurancesapp.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.InsuredsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsuredsScreen(
    getUserLogged:() -> String?,
    goToLogin:() -> Unit
) {
    val viewModel:InsuredsViewModel = InsuredsViewModel(getUserLogged,goToLogin);

    InsurancesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = { TopAppBar(
                    title = { Text(text = viewModel.userLogged.username) })
                }
            ) { padding ->
                val insureds:List<Insured>? = viewModel.insureds_.observeAsState().value;

                if(insureds != null)
                    InsuredsTable(
                        viewModel.insureds_.value!!,
                        modifier = Modifier.padding(padding)
                    );
                else
                    CircularProgressIndicator();
            }
        }
    }
}

@Composable
fun InsuredsTable(
    insureds:List<Insured>,
    modifier:Modifier = Modifier
) {
    LazyColumn(){
        items(insureds){ insured -> 
            InsuredItem(insured = insured)
        }
    }
}

@Composable
fun InsuredItem(insured:Insured) {
    Text(text = "${insured.firstname} ${insured.lastname}")
}