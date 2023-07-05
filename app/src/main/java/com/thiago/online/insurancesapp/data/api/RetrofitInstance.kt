package com.thiago.online.insurancesapp.data.api

import com.thiago.online.insurancesapp.data.api.endpoints.AuthEndpoint
import com.thiago.online.insurancesapp.data.api.endpoints.CompanyEndpoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val url:String = "http://192.168.0.211:5000/";
    private val retrofit:Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    public fun getAuthInstance(): AuthEndpoint {
        return retrofit.create(AuthEndpoint::class.java);
    }

    public fun getCompanyInstance(): CompanyEndpoint {
        return retrofit.create(CompanyEndpoint::class.java);
    }
}