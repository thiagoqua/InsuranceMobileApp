package com.thiago.online.insurancesapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.thiago.online.insurancesapp.data.api.services.AuthService
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.repositories.UserRepository
import com.thiago.online.insurancesapp.ui.Screens
import com.thiago.online.insurancesapp.ui.screens.InsuredsScreen
import com.thiago.online.insurancesapp.ui.screens.LogInScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    public lateinit var userRepository:UserRepository;
    @Inject
    public lateinit var authService: AuthService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContent {
            val checkingUser:MutableState<Boolean> = remember{ mutableStateOf(true) }
            val navController = rememberNavController();

            //starting page navigation
            NavHost(navController = navController, startDestination = Screens.LOG_IN_SCREEN.name){
                //in background check if there is a user logged and if it is valid
                checkUser(navController,checkingUser);

                //log in screen statement
                composable(Screens.LOG_IN_SCREEN.name){
                    LogInScreen(
                        { rememberingUser -> onLogInSuccess(rememberingUser,navController) },
                        checkingUser.value
                    );
                }

                //main screen statement
                composable(Screens.INSUREDS_SCREEN.name){
                    InsuredsScreen(
                        { getUser() },
                        { navController.navigate(Screens.LOG_IN_SCREEN.name) }
                    );
                }
            }
        }
    }

    private fun onLogInSuccess(user:String? = null,navController: NavController):Unit{
        /* null if the user doesn't click remember me*/
        if(user != null)
            storeUser(user);

        navController.navigate(Screens.INSUREDS_SCREEN.name);
    }

    private fun checkUser(
        navController: NavHostController,
        checking: MutableState<Boolean>
    ){
        GlobalScope.launch(Dispatchers.IO) {
            var userLogged:String? = getUser();

            if(userLogged != null && isValid(userLogged)){
                withContext(Dispatchers.Main){
                    userRepository.setUserState(
                        Gson().fromJson(userLogged,Admin::class.java)
                    );

                    navController.navigate(Screens.INSUREDS_SCREEN.name);
                }
            } else {
                withContext(Dispatchers.Main){
                    checking.value = false;
                }
            }
        }
    }

    private fun storeUser(userAsJson:String){
        with(getPreferences(MODE_PRIVATE).edit()){
            putString(getString(R.string.user_logged),userAsJson);
            apply();
        }
    }

    private fun getUser():String? {
        return getPreferences(Context.MODE_PRIVATE)
                .getString(getString(R.string.user_logged),null);
    }

    private suspend fun isValid(userAsJson: String):Boolean{
        val json:JsonObject = JsonParser.parseString(userAsJson).asJsonObject;
        val token:String = json.get("token").asString;

        try{
            return authService.checkTokenValid(token);
        } catch(ex:Exception){
            ex.printStackTrace()
            return false;
        }
    }
}