package com.thiago.online.insurancesapp.data.api.services

import com.thiago.online.insurancesapp.data.api.endpoints.AuthEndpoint
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.LogInRequest
import com.thiago.online.insurancesapp.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class AuthService @Inject constructor(
    private val instance:AuthEndpoint,
    private val userRepo:UserRepository
) {
    @Throws(IOException::class,RuntimeException::class)
    public suspend fun authenticate(request: LogInRequest): Admin?{
        val call: Response<Admin> = instance
            .authenticate(request)
            .execute();

        if(call.isSuccessful)
            return call.body()
        else
            return null;
    }

    @Throws(IOException::class,RuntimeException::class)
    public suspend fun checkTokenValid(token:String):Boolean{
        withContext(Dispatchers.Main){
            userRepo.setValidationToken(token);
        }

        val call:Response<Unit> = instance
            .checkTokenValidation()
            .execute();

        if(!call.isSuccessful()){
            withContext(Dispatchers.Main){
                userRepo.removeTokenValidation();
            }
            return false;
        }
        /* isn't neccesary to change the tokenValidation when the is call successfull
           becouse it is re-setted when the userState changes.
        */
        return true;
    }
}