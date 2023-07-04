package com.thiago.online.insurancesapp.models

data class Address(
    val id:Long,
    val street:String,
    val number:String,
    val floor:Int?,
    val departament:String?,
    val city:String,
    val province:String,
    val country:String
);
