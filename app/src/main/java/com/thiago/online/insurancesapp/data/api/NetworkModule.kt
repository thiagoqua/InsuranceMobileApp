package com.thiago.online.insurancesapp.data.api

import com.thiago.online.insurancesapp.data.api.endpoints.AuthEndpoint
import com.thiago.online.insurancesapp.data.api.endpoints.CompanyEndpoint
import com.thiago.online.insurancesapp.data.api.endpoints.InsuredEndpoint
import com.thiago.online.insurancesapp.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(userRepo:UserRepository):Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.211:5000/")
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(TokenInterceptor(userRepo))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    @Singleton
    @Provides
    public fun provideAuthInstance(retrofit: Retrofit): AuthEndpoint {
        return retrofit.create(AuthEndpoint::class.java);
    }

    @Singleton
    @Provides
    public fun provideCompanyInstance(retrofit: Retrofit): CompanyEndpoint {
        return retrofit.create(CompanyEndpoint::class.java);
    }

    @Singleton
    @Provides
    public fun provideInsuredInstance(retrofit: Retrofit): InsuredEndpoint {
        return retrofit.create(InsuredEndpoint::class.java);
    }
}