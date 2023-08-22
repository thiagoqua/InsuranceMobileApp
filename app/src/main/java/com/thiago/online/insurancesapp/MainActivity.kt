package com.thiago.online.insurancesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.thiago.online.insurancesapp.data.api.services.AuthService
import com.thiago.online.insurancesapp.data.helpers.SharedPreferencesHandler
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.repositories.UserRepository
import com.thiago.online.insurancesapp.ui.screens.DetailsScreen
import com.thiago.online.insurancesapp.ui.screens.DetailsScreenName
import com.thiago.online.insurancesapp.ui.screens.InsuredsScreen
import com.thiago.online.insurancesapp.ui.screens.InsuredsScreenName
import com.thiago.online.insurancesapp.ui.screens.LogInScreen
import com.thiago.online.insurancesapp.ui.screens.LogInScreenName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    public lateinit var userRepository:UserRepository;
    @Inject
    public lateinit var authService: AuthService;
    @Inject
    public lateinit var sharedPrefHandler:SharedPreferencesHandler;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContent {
            val checkingUser:MutableState<Boolean> = remember{ mutableStateOf(true) }
            val navController = rememberNavController();

            sharedPrefHandler.initializeWith(this);

            //starting page navigation
            NavHost(navController = navController, startDestination = LogInScreenName){
                //in background check if there is a user logged and if it is valid
                checkUser(navController,checkingUser);

                //log in screen statement
                composable(LogInScreenName){
                    LogInScreen(
                        { rememberingUser -> onLogInSuccess(rememberingUser,navController) },
                        checkingUser.value
                    );
                }
                //main screen statement
                composable(InsuredsScreenName){
                    InsuredsScreen(
                        { endSession(navController) },
                        { id -> navController.navigate("${DetailsScreenName}/${id}") }
                    );
                }
                //details screen statement
                composable(
                    route = "${DetailsScreenName}/{insuredId}",
                    arguments = listOf(
                        navArgument("insuredId"){ type = NavType.LongType }
                    )
                ){ navEntry ->
                    val id:Long = navEntry.arguments?.getLong("insuredId")!!;
                    DetailsScreen(id);
                }
            }
        }
    }

    private fun onLogInSuccess(
        user: String? = null,
        navController: NavController
    ):Unit{
        /* null if the user doesn't click remember me*/
        if(user != null)
            sharedPrefHandler.storeUser(user);

        navController.navigate(InsuredsScreenName);
    }

    private fun checkUser(
        navController: NavHostController,
        checking: MutableState<Boolean>
    ){
        GlobalScope.launch(Dispatchers.IO) {
            var userLogged:String? = sharedPrefHandler.getUser();

            if(userLogged != null && isValid(userLogged)){
                withContext(Dispatchers.Main){
                    userRepository.setUserState(
                        Gson().fromJson(userLogged,Admin::class.java)
                    );
                    checking.value = false;
                    navController.navigate(InsuredsScreenName);
                }
            } else {
                withContext(Dispatchers.Main){
                    checking.value = false;
                }
            }
        }
    }

    private fun endSession(navController: NavHostController):Unit{
        sharedPrefHandler.removeUser();
        userRepository.removeUserState();
        navController.navigate(LogInScreenName);
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