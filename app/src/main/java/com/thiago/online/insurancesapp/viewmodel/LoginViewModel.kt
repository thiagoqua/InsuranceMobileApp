package com.thiago.online.insurancesapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.thiago.online.insurancesapp.R
import com.thiago.online.insurancesapp.data.api.services.AuthService
import com.thiago.online.insurancesapp.data.models.LogInRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val sharedPref: SharedPreferences):ViewModel() {
    private val username = MutableLiveData<String>();
    private val password = MutableLiveData<String>();
    private val loginEnabled = MutableLiveData<Boolean>();
    private val loading = MutableLiveData<Boolean>();

    public val username_:LiveData<String> = username;
    public val password_:LiveData<String> = password;
    public val loginEnabled_:LiveData<Boolean> = loginEnabled;
    public val loading_:LiveData<Boolean> = loading;

    public fun usernameChanged(newValue:String){
        username.value = newValue;
        if(password.value != null)
            loginEnabled.value = username.value!!.isNotEmpty() &&
                                 password.value!!.isNotEmpty();
    }

    public fun passwordChanged(newValue:String){
        password.value = newValue;
        if(username.value != null)
            loginEnabled.value = username.value!!.isNotEmpty() &&
                                 password.value!!.isNotEmpty();
    }

    public fun onLogIn(){
        loading.value = true;
        CoroutineScope(Dispatchers.IO).launch {
            val service: AuthService = AuthService();
            val ret = service.authenticate(LogInRequest(username.value!!,password.value!!));

            if(ret == null) {
                withContext(Dispatchers.Main){
//                    loading.value = false;
                }
                TODO("imprimir mensaje de error")
            }

            with(sharedPref.edit()){
                val serializedAdmin:String = Gson().toJson(ret);
                putString("very_private_and_complicated_user_logged_key",serializedAdmin);
                apply();
            }
            withContext(Dispatchers.Main){
//                loading.value = false;
            }
        };
    }
}