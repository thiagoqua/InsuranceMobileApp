package com.thiago.online.insurancesapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thiago.online.insurancesapp.data.models.Address
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.data.models.Phone
import com.thiago.online.insurancesapp.ui.components.CircleLoader
import com.thiago.online.insurancesapp.ui.components.ErrorText
import com.thiago.online.insurancesapp.ui.components.Popup
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.DetailsViewModel

public const val DetailsScreenName:String = "DETAILS_SCREEN";
private val appBarTitle:MutableState<String> = mutableStateOf("");
private val makeDial:MutableState<Boolean> = mutableStateOf(false);

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
public fun DetailsScreen(
    insuredId:Long
) {
    val viewModel:DetailsViewModel = hiltViewModel<DetailsViewModel>();
    LaunchedEffect(key1 = insuredId){
        viewModel.initiliaizeWith(insuredId);
    }
    val insured:Insured? = viewModel.insured_.observeAsState().value;
    val error:String? = viewModel.error_.observeAsState().value;

    if (insured != null) {
        appBarTitle.value = "${insured.firstname} ${insured.lastname}"
    }

    BackHandler(enabled = false) {}

    InsurancesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = { DetailsAppBar(insured) }
            ) {
                if(error != null)
                    ErrorText(errorMessage = error);

                //is never null becouse when its null, the button is deactivated
                //is here becouse the popup needs to render over the screen
                if(makeDial.value)
                    onCallPressed(insured?.phones!!)

                if(insured != null)
                    InsuredDetails(insured);
                else
                    CircleLoader();
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsAppBar(insured:Insured?) {
    TopAppBar(
        title = { Text(
            text = appBarTitle.value,
            color = Color.White
        ) },
        actions = {
            IconButton(
                onClick = { makeDial.value = true },
                enabled = insured?.phones != null
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    tint = Color.White,
                    contentDescription = "call"
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    );
}

@Composable
private fun onCallPressed(phones: Array<Phone>) {
    val context:Context = LocalContext.current;

    if(phones.size > 1){
        val phonePrefix:String = "tel:";
        var phoneToCall:MutableState<String> = remember { mutableStateOf(phonePrefix) };
        val phoneSelectedId:MutableState<Long?> = remember { mutableStateOf(null) };

        Popup(
            enabled = makeDial,
            title = "Seleccione teléfono a llamar",
            mainText = {
                Column() {
                    phones.forEach { phone ->
                        TextButton(
                            onClick = {
                                phoneToCall.value = "${phonePrefix}${phone.number}";
                                phoneSelectedId.value = phone.id;
                            }
                        ) {
                            var text: String = "${phone.number}";
                            val color:Color =
                                if( phoneSelectedId.value != null &&
                                    phoneSelectedId.value == phone.id)
                                    MaterialTheme.colorScheme.primary;
                                else
                                    Color.Black;

                            if (phone.description != null)
                                text += " - ${phone.description}";

                            Text(text = text,color = color);
                        }
                    }
                };
            },
            onConfirm = {
                var uri:Uri = Uri.parse(phoneToCall.value);
                var dialIntent:Intent = Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(dialIntent);
                makeDial.value = false;
            }
        );
    }
    else {
        val uri:Uri = Uri.parse("tel:${phones[0].number}");
        val dialIntent:Intent = Intent(Intent.ACTION_DIAL,uri);
        context.startActivity(dialIntent);
        makeDial.value = false;
    }
}

@Composable
private fun InsuredDetails(insured:Insured) {
    val producerFullName:String =
        "${insured.producerNavigation.firstname} ${insured.producerNavigation.lastname}";
    val address:Address = insured.addressNavigation;
    val bornDate:String = insured.born.split('T')[0];

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(){
            DataTitle("Datos personales");
            Row(){
                InsuredDetailData(
                    "Nombre",
                    insured.firstname,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Apellido",
                    insured.lastname,
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "DNI",
                    insured.dni,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Fecha de nac.",
                    bornDate,
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row(){
                InsuredDetailData(
                    "CUIT",
                    insured.cuit,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Descripción",
                    insured.description,
                    Modifier.weight(1f)
                );
            }
            DataTitle("Dirección");
            Row(){
                InsuredDetailData(
                    "Calle",
                    address.street,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Número",
                    address.number,
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "Ciudad",
                    address.city,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Provincia",
                    address.province,
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "País",
                    address.country,
                    Modifier.weight(1f)
                );
                Spacer(modifier = Modifier.weight(1f));
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "Piso",
                    address.floor?.toString(),
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Depto.",
                    address.departament ?: null,
                    Modifier.weight(1f)
                );
            }
            DataTitle("Datos de la póliza");
            Row() {
                InsuredDetailData(
                    "Matrícula",
                    insured.license,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Carpeta",
                    insured.folder.toString(),
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "Vigencia",
                    insured.life,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Productor",
                    producerFullName,
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "Compañía",
                    insured.companyNavigation.name,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Vigencia de pago",
                    insured.paymentExpiration.toString(),
                    Modifier.weight(1f)
                );
            }
            Spacer(modifier = Modifier.padding(10.dp));
            Row() {
                InsuredDetailData(
                    "Estado",
                    insured.status,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Póliza de",
                    insured.insurancePolicy,
                    Modifier.weight(1f)
                );
            }
        }
    }
}

@Composable
private fun InsuredDetailData(
    label:String,
    value:String?,
    modifier: Modifier
) {
    Row(
        modifier = modifier
    ) {
        if(!value.isNullOrEmpty()) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                modifier = modifier,
                color = MaterialTheme.colorScheme.tertiary
            );
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            );
        }
        else {
            Text(
                text = label,
                modifier = modifier,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            );
        }
    }
}

@Composable
private fun DataTitle(text:String) {
    Text(
        text = text,
        modifier = Modifier.padding(20.dp),
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.tertiary
    );
}
