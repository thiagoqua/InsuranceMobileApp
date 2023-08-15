package com.thiago.online.insurancesapp.data.api.endpoints

import com.thiago.online.insurancesapp.data.models.Producer
import retrofit2.Call
import retrofit2.http.GET

interface ProducerEndpoint {
    @GET("/api/producer/all")
    fun getAll():Call<List<Producer>>;
}