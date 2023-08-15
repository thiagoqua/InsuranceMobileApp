package com.thiago.online.insurancesapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thiago.online.insurancesapp.data.api.services.ProducerService
import com.thiago.online.insurancesapp.data.helpers.NetworkExceptionResolver
import com.thiago.online.insurancesapp.data.models.Producer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProducerRepository @Inject constructor(
    private val service: ProducerService,
    private val resolver: NetworkExceptionResolver,
    private val userRepository: UserRepository
){
    private val producers = MutableLiveData<List<Producer>?>(null);
    private val error = MutableLiveData<String?>(null);

    public val error_: LiveData<String?> = error;
    public val producers_: LiveData<List<Producer>?> = producers;

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val res = resolver.resolveProducers(
                fn = { service.getAll(userRepository.userToken!!) },
                onError = { errorMessage -> error.value = errorMessage }
            );

            if(res != null){
                withContext(Dispatchers.Main){
                    producers.value = res;
                }
            }
        }
    }

    public fun findIdByFullName(name:String):Long =
        producers.value!!
            .find({ producer -> "${producer.firstname} ${producer.lastname}" == name})!!
            .id;
}