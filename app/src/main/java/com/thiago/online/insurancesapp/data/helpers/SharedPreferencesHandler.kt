package com.thiago.online.insurancesapp.data.helpers

import android.content.Context
import android.content.SharedPreferences
import com.thiago.online.insurancesapp.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHandler @Inject constructor() {
    private var sharedPrefs:SharedPreferences? = null;
    private var userLoggedKey:String? = null;

    public fun initializeWith(activityContext:Context):Unit {
        userLoggedKey = activityContext.getString(R.string.user_logged);
        sharedPrefs = activityContext.getSharedPreferences(
            activityContext.getString(R.string.shared_prefs),
            Context.MODE_PRIVATE
        );
    }

    public fun getUser():String? {
        return sharedPrefs!!.getString(userLoggedKey,null);
    }

    public fun storeUser(userAsJson:String):Unit{
        with(sharedPrefs!!.edit()){
            putString(userLoggedKey!!,userAsJson);
            apply();
        }
    }

    public fun removeUser():Unit{
        with(sharedPrefs!!.edit()){
            remove(userLoggedKey!!);
            apply();
        }
    }
}