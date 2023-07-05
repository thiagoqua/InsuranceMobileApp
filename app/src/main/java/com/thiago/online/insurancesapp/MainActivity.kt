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
import com.thiago.online.insurancesapp.data.models.LogInRequest
import com.thiago.online.insurancesapp.data.api.services.AuthService
import com.thiago.online.insurancesapp.ui.LogInScreen
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
                    LogInScreen(getPreferences(MODE_PRIVATE));
                }
            }
        }
    }
}