package com.thiago.online.insurancesapp.data.api.services

import com.thiago.online.insurancesapp.data.api.endpoints.ProducerEndpoint
import com.thiago.online.insurancesapp.data.models.Company
import com.thiago.online.insurancesapp.data.models.Producer
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

@Singleton
class ProducerService @Inject constructor(
    private val instance:ProducerEndpoint
){
    @Throws(IOException::class,RuntimeException::class)
    public suspend fun getAll(token:String):List<Producer>?{
        val call: Response<List<Producer>> = instance
            .getAll()
            .execute();

        if(call.isSuccessful)
            return call.body();
        else
            return null;
    }
}