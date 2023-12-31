package com.thiago.online.insurancesapp.data.api.services

import com.thiago.online.insurancesapp.data.api.endpoints.CompanyEndpoint
import com.thiago.online.insurancesapp.data.models.Company
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

@Singleton
class CompanyService @Inject constructor(
    private val instance:CompanyEndpoint
) {
    @Throws(IOException::class,RuntimeException::class)
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