package com.thiago.online.insurancesapp.data.repositories

import androidx.lifecycle.MutableLiveData
import com.thiago.online.insurancesapp.data.models.Admin
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    private val userState = MutableLiveData<Admin?>(null);
    private val userStateToken = MutableLiveData<String?>(null);

    public val userToken:String? get() = userStateToken.value;
    public val userLogged: Admin? get() = userState.value;
    public val isUserLogged:Boolean get() = userState.value != null;

    public fun setUserState(user:Admin):Unit{
        userState.value = user;
        userStateToken.value = user.token;
    }

    public fun removeUserState():Unit{
        userState.value = null;
        userStateToken.value = null;
    }

    public fun setValidationToken(toValidate:String):Unit{
        userStateToken.value = toValidate;
    }

    public fun removeTokenValidation():Unit {
        userStateToken.value = null;
    }
}