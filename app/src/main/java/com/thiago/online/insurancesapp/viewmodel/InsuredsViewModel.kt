package com.thiago.online.insurancesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thiago.online.insurancesapp.data.api.services.InsuredService
import com.thiago.online.insurancesapp.data.helpers.NetworkExceptionResolver
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.Company
import com.thiago.online.insurancesapp.data.models.Insured
import com.thiago.online.insurancesapp.data.models.Producer
import com.thiago.online.insurancesapp.data.repositories.CompanyRepository
import com.thiago.online.insurancesapp.data.repositories.ProducerRepository
import com.thiago.online.insurancesapp.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InsuredsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val producerRepository: ProducerRepository,
    private val service: InsuredService,
    private val exceptionResolver: NetworkExceptionResolver
):ViewModel() {
    private val insureds = MutableLiveData<List<Insured>>();        //can have the total or filtereds
    private val totalInsureds = MutableLiveData<List<Insured>>();   //always have the total
    private val error = MutableLiveData<String?>(null);
    private val areFiltersApplied = MutableLiveData<Boolean>(false);

    public var userLogged:Admin;
    public val insureds_:LiveData<List<Insured>> = insureds;
    public val error_:LiveData<String?> = error;
    public val areFiltersApplied_:LiveData<Boolean> = areFiltersApplied;

    init {
        userLogged = userRepository.userLogged!!;

        GlobalScope.launch(Dispatchers.IO) {
            val result = exceptionResolver.resolveInsureds(
                fn = { service.getAll(userLogged.token) },
                onError = { errorMessage ->
                    withContext(Dispatchers.Main){
                        error.value = errorMessage
                    }
                }
            );
            if(result != null) {
                withContext(Dispatchers.Main) {
                    insureds.value = result!!;
                    totalInsureds.value = result!!.toList();
                }
            }
        }
    }

    public fun search(query:String):Unit{
        GlobalScope.launch(Dispatchers.IO) {
            val result = exceptionResolver.resolveInsureds(
                fn = { service.search(query) },
                onError = { errorMessage ->
                    withContext(Dispatchers.Main){
                        error.value = errorMessage
                    }
                }
            );
            if(result != null) {
                withContext(Dispatchers.Main) {
                    insureds.value = result!!;
                    if(!error.value.isNullOrEmpty())
                        error.value = null;
                }
            }
        }
    }

    public fun showAll():Unit{
        insureds.value = totalInsureds.value;
        areFiltersApplied.value = false;
    }

    //if an item is null, the filter is not counted. else, find its id to pass
    //to the api
    public fun resolveFilters(filters: Array<String?>) {
        val apiFilters:Array<String?> = arrayOfNulls(4);
        var filtersApplied:MutableList<String> = mutableListOf();

        if(filters[0] != null) {
            apiFilters[0] = companyRepository.findIdByName(filters[0]!!).toString();
            filtersApplied.add(filters[0]!!);
        }
        if(filters[1] != null) {
            apiFilters[1] = producerRepository.findIdByFullName(filters[1]!!).toString();
            filtersApplied.add(filters[1]!!);
        }
        if(filters[2] != null){
            apiFilters[2] = filters[2];
            filtersApplied.add(filters[2]!!);
        }
        if(filters[3] != null){
            apiFilters[3] = filters[3];
            filtersApplied.add(filters[3]!!);
        }

        if(apiFilters.filterNotNull().size > 0)
            getWithFilters(apiFilters);
    }

    public fun companies():LiveData<List<Company>?> = companyRepository.companies_;

    public fun producers():LiveData<List<Producer>?> = producerRepository.producers_;

    private fun getWithFilters(filters: Array<String?>){
        GlobalScope.launch(Dispatchers.IO){
            val result = exceptionResolver.resolveInsureds(
                fn = { service.findByFilters(filters) },
                onError = { errorMessage ->
                    withContext(Dispatchers.Main){
                        error.value = errorMessage
                    }
                }
            );

            if(result != null){
                withContext(Dispatchers.Main){
                    insureds.value = result!!;
                    if(!error.value.isNullOrEmpty())
                        error.value = null;
                    areFiltersApplied.value = true;
                }
            }
        }
    }
}