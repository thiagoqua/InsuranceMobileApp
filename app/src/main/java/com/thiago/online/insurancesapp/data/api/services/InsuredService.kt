package com.thiago.online.insurancesapp.data.api.services

import android.content.res.Resources.NotFoundException
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.thiago.online.insurancesapp.data.api.endpoints.InsuredEndpoint
import com.thiago.online.insurancesapp.data.models.Insured
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import kotlin.jvm.Throws

class InsuredService @Inject constructor(
    private val instance:InsuredEndpoint
){
    @Throws(IOException::class,RuntimeException::class,NotFoundException::class)
    public suspend fun getAll(token:String):List<Insured>{
        val call:Response<JsonArray> = instance
            .getAll()
            .execute();

        if(!call.isSuccessful())        //missing JWT or invalid
            throw NotFoundException();

        return call.body()!!.map { element ->
                    Gson().fromJson(element,Insured::class.java)
                };
    }

    @Throws(IOException::class,RuntimeException::class,NotFoundException::class)
    public suspend fun search(query: String):List<Insured> {
        val call:Response<JsonArray> = instance
            .get(query)
            .execute();

        if(!call.isSuccessful())        //missing JWT or invalid
            throw NotFoundException();

        return if(call.body() != null && call.body()!!.count() > 0)
                    call.body()!!.map { element ->
                        Gson().fromJson(element,Insured::class.java)
                    }
                else
                    listOf();
    }

    @Throws(IOException::class,RuntimeException::class,NotFoundException::class)
    public suspend fun findById(id:Long):Insured{
        val call:Response<Insured> = instance
            .getById(id.toString())
            .execute();

        if(!call.isSuccessful())       //missing JWT or invalid
            throw NotFoundException()

        return call.body()!!;
    }

    @Throws(IOException::class,RuntimeException::class,NotFoundException::class)
    public suspend fun findByFilters(filters: Array<String?>): List<Insured> {
        val call:Response<JsonArray> = instance
            .getByFilters(
                company = filters[0],
                producer = filters[1],
                lifestart = filters[2],
                status = filters[3]
            )
            .execute();

        if(!call.isSuccessful())        //missing JWT or invalid
            throw NotFoundException();

        return if(call.body() != null && call.body()!!.count() > 0)
            call.body()!!.map { element ->
                Gson().fromJson(element,Insured::class.java)
            }
        else
            listOf();
    }
}