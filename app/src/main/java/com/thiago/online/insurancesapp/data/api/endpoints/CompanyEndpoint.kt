package com.thiago.online.insurancesapp.data.api.endpoints

import com.thiago.online.insurancesapp.data.models.Company
import retrofit2.Call
import retrofit2.http.GET

interface CompanyEndpoint {
    @GET("/api/company/all")
    fun getAll(): Call<List<Company>>;
}