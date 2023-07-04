package com.thiago.online.insurancesapp.api.services

import com.thiago.online.insurancesapp.api.RetrofitInstance
import com.thiago.online.insurancesapp.models.Admin
import com.thiago.online.insurancesapp.models.Company
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