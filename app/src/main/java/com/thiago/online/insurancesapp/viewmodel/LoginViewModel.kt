package com.thiago.online.insurancesapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.thiago.online.insurancesapp.R
import com.thiago.online.insurancesapp.data.api.services.AuthService
import com.thiago.online.insurancesapp.data.models.LogInRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val storeUser:(String) -> Unit):ViewModel() {
    private val username = MutableLiveData<String>();
    private val password = MutableLiveData<String>();
    private val loading = MutableLiveData<Boolean>(false);
    private val error = MutableLiveData<String?>(null);

    public val username_:LiveData<String> = username;
    public val password_:LiveData<String> = password;
    public val loading_:LiveData<Boolean> = loading;
    public val error_:LiveData<String?> = error;

    private val user_logged_key:String = "very_private_and_complicated_user_logged_key";

    public fun usernameChanged(newValue:String){
        username.value = newValue;
    }

    public fun passwordChanged(newValue:String){
        password.value = newValue;
    }

    public fun onLogIn(onSuccess:() -> Unit,rememberUser:Boolean){
        loading.value = true;
        error.value = "";

        GlobalScope.launch(Dispatchers.IO) {
            val service: AuthService = AuthService();
            val ret = service.authenticate(LogInRequest(username.value!!,password.value!!));

            if(ret == null) {
                withContext(Dispatchers.Main){
                    loading.value = false;
                    error.value = "Credenciales invalidas";
                }
            }
            else {
                if(rememberUser){
                    val serializedAdmin:String = Gson().toJson(ret);
                    storeUser(serializedAdmin);
                }

                withContext(Dispatchers.Main){
                    loading.value = false;
                    onSuccess();
                }
            }
        };
    }
}