package com.thiago.online.insurancesapp.data.api.endpoints

import com.google.gson.JsonArray
import com.thiago.online.insurancesapp.data.models.Insured
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InsuredEndpoint {
    @GET("/api/insured/all")
    fun getAll(): Call<JsonArray>;
    @GET("/api/insured/search")
    fun get(@Query("query") query:String): Call<JsonArray>;
    @GET("/api/insured/{id}")
    fun getById(@Path("id") id:String): Call<Insured>;
}