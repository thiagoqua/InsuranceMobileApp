package com.thiago.online.insurancesapp.data.api.endpoints

import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.LogInRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthEndpoint {
    @POST("/api/auth")
    fun authenticate(@Body request: LogInRequest): Call<Admin>;
}