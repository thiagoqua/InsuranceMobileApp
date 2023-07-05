package com.thiago.online.insurancesapp.data.models

import java.time.LocalDateTime

data class Insured(
    val id:Long,
    val firstname:String,
    val lastname:String,
    val license:String,
    val folder:Int,
    val life:String,
    val born:LocalDateTime,
    val address:Long,
    val dni:String,
    val cuit:String,
    val producer:Int,
    val description:String,
    val company:Int,
    val insurancePolicy:String,
    val status:String,
    val paymentExpiration:Short,
    val phones:Array<Phone>,
    val addressNavigation: Address,
    val companyNavigation: Company,
    val producerNavigation: Producer,
);
