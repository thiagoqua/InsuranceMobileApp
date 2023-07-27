package com.thiago.online.insurancesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thiago.online.insurancesapp.data.api.services.InsuredService
import com.thiago.online.insurancesapp.data.helpers.NetworkExceptionResolver
import com.thiago.online.insurancesapp.data.models.Insured
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val service: InsuredService,
    private val resolver: NetworkExceptionResolver
):ViewModel(){
    private val insured = MutableLiveData<Insured>();
    private val error = MutableLiveData<String>();

    public val insured_:LiveData<Insured> = insured;
    public val error_:LiveData<String> = error;

    public fun initiliaizeWith(id:Long){
        GlobalScope.launch(Dispatchers.IO) {
            val ret = resolver.resolveInsured(
                fn = { service.findById(id) },
                onError = { errorMessage ->
                    withContext(Dispatchers.Main){
                        error.value = errorMessage;
                    }
                }
            );

            if(ret != null)
                withContext(Dispatchers.Main){
                    insured.value = ret!!;
                }
        }
    }
}