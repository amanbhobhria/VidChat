package com.example.vidchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidchat.Repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository):ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState:LiveData<LoginState> get()=_loginState


    fun login(email:String,password:String)
    {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email and Password must not be empty")
            return
        }


        viewModelScope.launch {
            _loginState.value=LoginState.Loading
            val result = authRepository.login(email,password)
            result.onSuccess { user->
                _loginState.value=LoginState.Success(user)

            }.onFailure { exception ->
                _loginState.value=LoginState.Error(exception.message ?:"Login failed")
            }
        }
    }


    sealed class LoginState {
        object Loading:LoginState()
        data class Success(val user:FirebaseUser?):LoginState()
        data class Error(val message:String):LoginState()



    }




}