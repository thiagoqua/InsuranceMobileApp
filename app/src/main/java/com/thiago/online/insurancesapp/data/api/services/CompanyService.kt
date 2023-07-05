package com.thiago.online.insurancesapp.data.api.services

import com.thiago.online.insurancesapp.data.api.RetrofitInstance
import com.thiago.online.insurancesapp.data.models.Company
import retrofit2.Response

class CompanyService {
    public suspend fun getAll():List<Company>?{
        val call: Response<List<Company>> = RetrofitInstance.getCompanyInstance()
            .getAll()
            .execute();

        if(call.isSuccessful)
            return call.body();
        else
            return null;
    }
}