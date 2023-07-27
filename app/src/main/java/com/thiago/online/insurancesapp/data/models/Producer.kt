package com.thiago.online.insurancesapp.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Producer(
    val id:Long,
    val firstname:String,
    val lastname:String,
    val joined:String,
    val code:Int
);
