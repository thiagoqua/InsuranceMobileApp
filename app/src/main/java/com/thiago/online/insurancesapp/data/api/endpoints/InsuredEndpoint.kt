package com.thiago.online.insurancesapp.data.api.endpoints

import com.google.gson.JsonArray
import com.thiago.online.insurancesapp.data.models.Insured
import retrofit2.Call
import retrofit2.http.GET

interface InsuredEndpoint {
    @GET("/api/insured/all")
    fun getAll(): Call<JsonArray>;
}