package com.pwr.sailapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.MockUserAuthentication
import com.pwr.sailapp.utils.CredentialsUtil

/*
https://developer.android.com/guide/navigation/navigation-conditional#kotlin
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val appContext = application.applicationContext

    enum class AuthenticationState{
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION
    }
    val authenticationState = MutableLiveData<AuthenticationState>()

    init{
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun refuseAuthentication(){
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authenticate(email: String, password:String){
        if(isPasswordValidForEmail(email, password)){
            authenticationState.value = AuthenticationState.AUTHENTICATED
            // Save user details to shared preferences
            CredentialsUtil.saveUserCredentials(appContext, email, password)
        }
        else authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
    }

    private fun isPasswordValidForEmail(email: String, password:String):Boolean {
        // TODO implement mock authentication (API)
        return MockUserAuthentication.authenticateUser(email, password)
    }
}