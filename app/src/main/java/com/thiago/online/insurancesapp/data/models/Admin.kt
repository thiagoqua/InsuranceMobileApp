package com.thiago.online.insurancesapp.data.models

data class Admin(
    val username:String,
    val token:String?,
    val producer:Long,
    val producerNavigation: Producer
);