package com.thiago.online.insurancesapp.data.helpers

import android.content.res.Resources
import com.thiago.online.insurancesapp.data.models.Admin
import com.thiago.online.insurancesapp.data.models.Insured
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkExceptionResolver @Inject constructor(){
    private suspend fun resolve(
        fn:suspend () -> Any?,
        onError:suspend (String) -> Unit
    ):Any?{
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

    public suspend fun resolveLogIn(
        fn:suspend () -> Admin?,
        onError:suspend (String) -> Unit
    ):Admin?{
        val ret = resolve(
            fn,
            onError
        );

        if(ret != null && ret is Admin)
            return ret;

        return null;
    }

    public suspend fun resolveInsureds(
        fn:suspend () -> List<Insured>,
        onError:suspend (String) -> Unit
    ):List<Insured>?{
        val ret = resolve(
            fn,
            onError
        );

        if(ret != null && ret is List<*>)
            return ret as List<Insured>;

        return null;
    }

    public suspend fun resolveInsured(
        fn:suspend () -> Insured,
        onError:suspend (String) -> Unit
    ):Insured?{
        val ret = resolve(
            fn,
            onError
        );

        if(ret != null && ret is Insured)
            return ret;

        return null;
    }
}