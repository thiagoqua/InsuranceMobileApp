package com.thiago.online.insurancesapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.gson.Gson
import com.thiago.online.insurancesapp.ui.theme.InsurancesAppTheme
import com.thiago.online.insurancesapp.components.Form
import com.thiago.online.insurancesapp.models.LogInRequest
import com.thiago.online.insurancesapp.api.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            InsurancesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Form({ username:String,password:String -> authenticate(username,password)});
                }
            }
        }
    }

    fun authenticate(username:String,passwod:String):Unit{
        CoroutineScope(Dispatchers.IO).launch {
          val service: AuthService = AuthService();
          val ret = service.authenticate(LogInRequest(username,passwod));
          if(ret == null) {
              withContext(Dispatchers.Main) {
                  Toast.makeText(
                      this@MainActivity,
                      "Username or password are not correct",
                      Toast.LENGTH_LONG
                  ).show();
              }
              return@launch;
          }

          val sharedPref = getPreferences(Context.MODE_PRIVATE);
          with(sharedPref.edit()){
              val serializedAdmin:String = Gson().toJson(ret);
              putString(getString(R.string.user_logged),serializedAdmin);
              apply();
          }
        };
    }
}