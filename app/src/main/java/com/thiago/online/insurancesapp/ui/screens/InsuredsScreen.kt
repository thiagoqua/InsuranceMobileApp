package com.thiago.online.insurancesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.ui.components.CircleLoader
import com.thiago.online.insurancesapp.ui.components.ErrorText
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.InsuredsViewModel

const val InsuredsScreenName:String = "MAIN_SCREEN";

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun InsuredsScreen(
    getUserLogged:() -> String?,
    onEndSession:() -> Unit,
    onDetails:(Long) -> Unit
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
                            { query -> viewModel.search(query) },
                            { viewModel.showAll() },
                            modifier = Modifier.padding(padding)
                        );
                        if(error != null)
                            ErrorText(errorMessage = error);
                        InsuredsTable(
                            viewModel.insureds_.value!!,
                            modifier = Modifier.padding(padding),
                            onDetails
                        );
                    }
                }
                else
                    CircleLoader();
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(
    username: String,
    onCloseSession: () -> Unit
){
    TopAppBar(
        title = { Text(text = username) },
        actions = {
            IconButton(
                onClick = { onCloseSession() },
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

    if(text.value.isEmpty())
        showAll();

    Row(
        modifier = modifier
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
            enabled = text.value.isNotEmpty()
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
    insureds: List<Insured>,
    modifier: Modifier = Modifier,
    onDetails: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ){
        items(insureds){ insured -> 
            InsuredItem(
                insured = insured,
                { onDetails(insured.id) }
            );
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun InsuredItem(
    insured: Insured,
    seeDetails: () -> Unit
) {
    val logoUrl:String = insured.companyNavigation.logo;

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InsuredInfo(
            insured,
            Modifier.weight(1f)
        );
        Image(
            painter = rememberAsyncImagePainter(logoUrl),
            contentDescription = "company_image",
            modifier = Modifier
                .size(80.dp)
                .weight(0.5f)
        );
        IconButton(
            onClick = seeDetails,
            modifier = Modifier.weight(0.5f)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "more_info",
                modifier = Modifier.size(60.dp)
            );
        }
    }
    Spacer(modifier = Modifier.padding(16.dp));
}

@Composable
fun InsuredInfo(
    insured: Insured,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        val producerName:String = insured.producerNavigation.firstname;
        val policyOf:String? = insured.insurancePolicy;
        val status:String = insured.status;

        Text(
            text = "${insured.firstname} ${insured.lastname}",
            modifier = Modifier
        );
        Text(
            text = "Productor: ${producerName}"
        );
        if(!policyOf.isNullOrEmpty())
            Text(
                text = policyOf
            );
    }
}
