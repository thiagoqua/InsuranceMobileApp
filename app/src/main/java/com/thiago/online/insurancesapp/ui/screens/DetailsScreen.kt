package com.thiago.online.insurancesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thiago.online.insurancesapp.data.models.Address
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.data.models.Phone
import com.thiago.online.insurancesapp.ui.components.CircleLoader
import com.thiago.online.insurancesapp.ui.components.ErrorText
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.viewmodel.DetailsViewModel

const val DetailsScreenName:String = "DETAILS_SCREEN";

@Composable
fun DetailsScreen(
    insuredId:Long
) {
    val viewModel:DetailsViewModel = hiltViewModel<DetailsViewModel>();
    val insured:Insured? = viewModel.insured_.observeAsState().value;
    val error:String? = viewModel.error_.observeAsState().value;

    viewModel.initiliaizeWith(insuredId);

    InsurancesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if(error != null)
                ErrorText(errorMessage = error);

            if(insured != null)
                InsuredDetails(insured);
            else
                CircleLoader();
        }
    }
}

@Composable
fun InsuredDetails(insured:Insured) {
    val producerFullName:String =
        "${insured.producerNavigation.firstname} ${insured.producerNavigation.lastname}";
    val address:Address = insured.addressNavigation;
    val phoneText:String = if(insured.phones.count() > 1) "Teléfonos" else "Teléfono";
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
            DataTitle(phoneText);
            InsuredDetailPhones(insured.phones);

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
fun InsuredDetailData(
    label:String,
    value:String?,
    modifier: Modifier
) {
    if(!value.isNullOrEmpty()) {
        Row(
            modifier = modifier
        ) {
            Text(
                text = label,
                modifier = modifier
            );
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            );
        }
    }
    else
        Spacer(modifier = modifier);
}

@Composable
fun InsuredDetailPhones(phones:Array<Phone>) {
    val totalPhones:Int = phones.count();

    Column(){
        Column() {
            InsuredDetailData(
                "Número de teléfono",
                phones[0].number,
                Modifier.weight(1f)
            );
            InsuredDetailData(
                "Descripción",
                phones[0].description,
                Modifier.weight(1f)
            );
        }
        if(totalPhones > 1){
            Column() {
                InsuredDetailData(
                    "Número de teléfono",
                    phones[1].number,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Descripción",
                    phones[1].description,
                    Modifier.weight(1f)
                );
            }
        }
        if(totalPhones > 2){
            Column() {
                InsuredDetailData(
                    "Número de teléfono",
                    phones[2].number,
                    Modifier.weight(1f)
                );
                InsuredDetailData(
                    "Descripción",
                    phones[2].description,
                    Modifier.weight(1f)
                );
            }
        }
    }
}

@Composable
fun DataTitle(text:String) {
    Text(
        text = text,
        modifier = Modifier.padding(20.dp),
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Blue
    )
}
