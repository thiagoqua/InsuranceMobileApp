package com.thiago.online.insurancesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thiago.online.insurancesapp.data.api.services.InsuredService
import com.thiago.online.insurancesapp.data.helpers.NetworkExceptionResolver
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.Insured
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
    private val service: InsuredService,
    private val exceptionResolver: NetworkExceptionResolver
):ViewModel() {
    private val insureds = MutableLiveData<List<Insured>>();        //can have the total or filtereds
    private val totalInsureds = MutableLiveData<List<Insured>>();   //always have the total
    private val error = MutableLiveData<String?>(null);

    public var userLogged:Admin;
    public val insureds_:LiveData<List<Insured>> = insureds;
    public val error_:LiveData<String?> = error;

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
                }
            }
        }
    }

    public fun showAll():Unit{
        insureds.value = totalInsureds.value;
    }
}