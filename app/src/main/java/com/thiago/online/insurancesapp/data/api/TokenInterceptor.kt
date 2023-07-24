package com.thiago.online.insurancesapp.data.api

import com.thiago.online.insurancesapp.data.repositories.UserRepository
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(var userRepo: UserRepository):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token:String = userRepo.userToken ?: "";

        val request = chain.request().newBuilder()
            .addHeader("Authorization","Bearer ${token}")
            .build();

        return chain.proceed(request);
    }
}