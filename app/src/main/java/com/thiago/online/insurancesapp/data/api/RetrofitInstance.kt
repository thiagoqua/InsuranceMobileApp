package com.thiago.online.insurancesapp.data.api

import com.thiago.online.insurancesapp.data.api.endpoints.AuthEndpoint
import com.thiago.online.insurancesapp.data.api.endpoints.CompanyEndpoint
import com.thiago.online.insurancesapp.data.api.endpoints.InsuredEndpoint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private val url:String = "http://192.168.0.211:5000/";

    private fun retrofit(token:String?):Retrofit{
        if(token == null)
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        else
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient().newBuilder()
                        .addInterceptor(TokenInterceptor(token))
                        .build()
                )
                .build();
    }

    public fun getAuthInstance(): AuthEndpoint {
        return retrofit(null).create(AuthEndpoint::class.java);
    }

    public fun getCompanyInstance(): CompanyEndpoint {
        return retrofit(null).create(CompanyEndpoint::class.java);
    }

    public fun getInsuredInstance(token:String): InsuredEndpoint{
        return retrofit(token).create(InsuredEndpoint::class.java);
    }
}