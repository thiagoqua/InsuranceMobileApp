package com.thiago.online.insurancesapp.data.helpers

import android.content.res.Resources
import com.thiago.online.insurancesapp.data.models.Insured
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkExceptionResolver @Inject constructor(){
    public suspend fun resolveInsureds(
        fn:suspend () -> List<Insured>,
        onError:suspend (String) -> Unit
    ):List<Insured>?{
        try{
            val ret = fn();
            return ret;
        } catch(ex: IOException) {           //failed to connect to the server
            onError("Hubo un problema al conectarse con el servidor. " +
                    "Por favor, comuníquese con los desarrolladores.");
        } catch(ex:RuntimeException) {      //error in the request or in the response
            ex.printStackTrace()
            onError("Hubo un problema con la respuesta del servidor. " +
                    "Por favor, comuníquese con los desarrolladores.");
        } catch(ex: Resources.NotFoundException) {  //response code is 401
            onError("Autenticación inválida. Por favor, reinicie la app " +
                    "y logueese nuevamente.");
        }

        return null;
    }
}