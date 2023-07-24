package com.thiago.online.insurancesapp.data.api.services

import android.content.res.Resources.NotFoundException
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.thiago.online.insurancesapp.data.api.endpoints.InsuredEndpoint
import com.thiago.online.insurancesapp.data.models.Insured
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class InsuredService @Inject constructor(
    private val instance:InsuredEndpoint
){
    public suspend fun getAll(token:String):List<Insured>?{
        val call:Response<JsonArray> = instance
            .getAll()
            .execute();

        if(call.isSuccessful)
            return call.body()!!
                    .map { element ->
                        Gson().fromJson(element,Insured::class.java)
                    }
        else if(call.raw().code() == 401)
            throw NotFoundException();
        else
            throw Exception(call.raw().message());
    }
}