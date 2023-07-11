package com.thiago.online.insurancesapp.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Producer(
    val id:Long,
    val Firstname:String,
    val Lastname:String,
    val Joined:String,
    val Code:Int
);
