package com.thiago.online.insurancesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.ui.components.ErrorText
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.InsuredsViewModel

const val InsuredsScreenName:String = "MAIN_SCREEN";

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun InsuredsScreen(
    getUserLogged:() -> String?,
    onEndSession:() -> Unit
) {
    val viewModel:InsuredsViewModel = hiltViewModel<InsuredsViewModel>();

    InsurancesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = { AppBar(
                    viewModel.userLogged.username,
                    { onEndSession() }
                )}
            ) { padding ->
                val insureds:List<Insured>? = viewModel.insureds_.observeAsState().value;
                val error:String? = viewModel.error_.observeAsState().value;

                if(insureds != null) {
                    Column() {
                        InsuredsSearcher(
                            {query -> viewModel.search(query)},
                            { viewModel.showAll() },
                            modifier = Modifier.padding(padding)
                        );
                        if(error != null)
                            ErrorText(errorMessage = error);
                        InsuredsTable(
                            viewModel.insureds_.value!!,
                            modifier = Modifier.padding(padding)
                        );
                    }
                }
                else
                    CircularProgressIndicator();
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(
    username: String,
    closeSession: () -> Unit
){
    TopAppBar(
        title = { Text(text = username) },
        actions = {
            IconButton(
                onClick = { closeSession() },
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "End Session"
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.LightGray)
    );
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun InsuredsSearcher(
    search:(String) -> Unit,
    showAll:() -> Unit,
    modifier:Modifier = Modifier
) {
    val text:MutableState<String> = remember { mutableStateOf<String>("") }

    if(text.value == "")
        showAll();

    Row(modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it},
            label = { Text(text = "Thiago, Quaglia, etc") }
        );
        IconButton(
            onClick = { search(text.value) },
            enabled = text.value != ""
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun InsuredsTable(
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
@OptIn(ExperimentalMaterial3Api::class)
private fun InsuredItem(insured:Insured) {
    Text(text = "${insured.firstname} ${insured.lastname}")
}
