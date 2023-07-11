package com.thiago.online.insurancesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.thiago.online.insurancesapp.ui.Screens
import com.thiago.online.insurancesapp.ui.screens.InsuredsScreen
import com.thiago.online.insurancesapp.ui.screens.LogInScreen
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            val navController = rememberNavController();
            NavHost(navController = navController, startDestination = startScreen().name){
                composable(Screens.LOG_IN_SCREEN.name){
                    LogInScreen(
                        { json -> storeUser(json) },
                        { navController.navigate(Screens.INSUREDS_SCREEN.name) }
                    );
                }
                composable(Screens.INSUREDS_SCREEN.name){
                    InsuredsScreen(
                        { getUser() },
                        { navController.navigate(Screens.LOG_IN_SCREEN.name) }
                    );
                }
            }
        }
    }

    private fun startScreen():Screens{
        var ret:Screens;

        if(getUser() === "")
            ret = Screens.LOG_IN_SCREEN;
        else
            ret = Screens.INSUREDS_SCREEN;

        return ret;
    }

    private fun storeUser(userAsJson:String){
        with(getPreferences(MODE_PRIVATE).edit()){
            putString(getString(R.string.user_logged),userAsJson);
            putString(getString(R.string.user_logged_creation),LocalDateTime.now().toString());
            apply();
        }
    }

    private fun getUser():String? {
//        TODO("set the correct value to userExpirationDate " +
//                "and make an endpoint")
        val prefs:SharedPreferences = getPreferences(Context.MODE_PRIVATE);
//        val userCreationKey:String = getString(R.string.user_logged_creation);
//        val userCreationDate:LocalDateTime =
//            LocalDateTime.parse(prefs.getString(userCreationKey,null));
//        val userExpirationDate:LocalDateTime = LocalDateTime.now();
//
//        if(userCreationDate.compareTo(userExpirationDate) > 0)
//            return null;
//        else
            return prefs.getString(getString(R.string.user_logged),null);

    }
}