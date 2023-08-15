package com.thiago.online.insurancesapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thiago.online.insurancesapp.data.api.services.CompanyService
import com.thiago.online.insurancesapp.data.helpers.NetworkExceptionResolver
import com.thiago.online.insurancesapp.data.models.Company
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRepository @Inject constructor(
    private val service:CompanyService,
    private val userRepository:UserRepository,
    private val resolver:NetworkExceptionResolver
){
    private val companies = MutableLiveData<List<Company>?>(null);
    private val error = MutableLiveData<String?>(null);

    public val error_:LiveData<String?> = error;
    public val companies_:LiveData<List<Company>?> = companies;

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val res = resolver.resolveCompanies(
                fn = { service.getAll(userRepository.userToken!!) },
                onError = { errorMessage -> error.value = errorMessage }
            );

            if(res != null){
                withContext(Dispatchers.Main){
                    companies.value = res;
                }
            }
        }
    }

    public fun findIdByName(companyName:String):Long =
        companies.value!!
            .find({ company -> company.name == companyName})!!
            .id;
}