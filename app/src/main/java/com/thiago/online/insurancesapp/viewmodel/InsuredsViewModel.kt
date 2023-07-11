package com.thiago.online.insurancesapp.viewmodel

import android.content.SharedPreferences
import android.content.res.Resources.NotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.thiago.online.insurancesapp.MainActivity
import com.thiago.online.insurancesapp.R
import com.thiago.online.insurancesapp.data.api.services.InsuredService
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.Insured
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsuredsViewModel(
    getUserLogged:() -> String?,
    goToLogin:() -> Unit
):ViewModel() {
    private val insureds = MutableLiveData<List<Insured>>();
    private val service:InsuredService = InsuredService();
    public var userLogged:Admin;

    public val insureds_:LiveData<List<Insured>> = insureds;

    init {
        if(getUserLogged == null)
            goToLogin();

        userLogged = Gson().fromJson(getUserLogged(),Admin::class.java);

        GlobalScope.launch(Dispatchers.IO) {
            try{
                val res = service.getAll(userLogged.token);
                withContext(Dispatchers.Main){
                    insureds.value = res;
                }
            } catch(ex:NotFoundException) {
                ex.printStackTrace();
            }
        }
    }
}