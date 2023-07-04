package com.thiago.online.insurancesapp.api.endpoints

import com.thiago.online.insurancesapp.models.Admin
import com.thiago.online.insurancesapp.models.LogInRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthEndpoint {
    @POST("/api/auth")
    fun authenticate(@Body request: LogInRequest): Call<Admin>;
}