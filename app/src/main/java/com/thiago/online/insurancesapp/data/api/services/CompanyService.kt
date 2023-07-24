package com.thiago.online.insurancesapp.data.api.services

import com.thiago.online.insurancesapp.data.api.endpoints.CompanyEndpoint
import com.thiago.online.insurancesapp.data.models.Company
import retrofit2.Response
import javax.inject.Inject

class CompanyService @Inject constructor(
    private val instance:CompanyEndpoint
) {
    public suspend fun getAll(token:String):List<Company>?{
        val call: Response<List<Company>> = instance
            .getAll()
            .execute();

        if(call.isSuccessful)
            return call.body();
        else
            return null;
    }
}