package com.thiago.online.insurancesapp.models

import java.time.LocalDateTime

data class Producer(
    val id:Long,
    val Firstname:String,
    val Lastname:String,
    val Joined:LocalDateTime,
    val Code:Int
);
