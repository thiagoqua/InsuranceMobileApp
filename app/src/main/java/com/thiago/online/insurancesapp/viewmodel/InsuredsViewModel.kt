package com.thiago.online.insurancesapp.viewmodel

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thiago.online.insurancesapp.data.api.services.InsuredService
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
    private val service: InsuredService
):ViewModel() {
    private val insureds = MutableLiveData<List<Insured>>();
    public var userLogged:Admin;

    public val insureds_:LiveData<List<Insured>> = insureds;

    init {
        userLogged = userRepository.userLogged!!;

        GlobalScope.launch(Dispatchers.IO) {
            try{
                val res = service.getAll(userLogged.token);
                withContext(Dispatchers.Main){
                    insureds.value = res!!;
                    // TODO("SOLVE IT")
                }
            } catch(ex:NotFoundException) {
                ex.printStackTrace();
            }
        }
    }
}