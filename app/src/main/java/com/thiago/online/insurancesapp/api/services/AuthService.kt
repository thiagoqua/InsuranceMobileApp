package com.thiago.online.insurancesapp.api.services

import com.thiago.online.insurancesapp.api.RetrofitInstance
import com.thiago.online.insurancesapp.models.Admin
import com.thiago.online.insurancesapp.models.LogInRequest
import retrofit2.Response

class AuthService() {
    public suspend fun authenticate(request: LogInRequest):Admin?{
        val call: Response<Admin> = RetrofitInstance.getAuthInstance()
            .authenticate(request)
            .execute();

        if(call.isSuccessful)
            return call.body()
        else
            return null;

    }
}