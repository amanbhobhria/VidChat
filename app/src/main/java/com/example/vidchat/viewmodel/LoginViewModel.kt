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

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> get() = _signupState





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

    fun signup(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _signupState.value = SignupState.Error("Email and Password must not be empty")
            return
        }

        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            val result = authRepository.signup(email, password)
            result.onSuccess { user ->
                _signupState.value = SignupState.Success(user)
            }.onFailure { exception ->
                _signupState.value = SignupState.Error(exception.message ?: "Signup failed")
            }
        }
    }




    sealed class LoginState {
        object Loading:LoginState()
        data class Success(val user:FirebaseUser?):LoginState()
        data class Error(val message:String):LoginState()



    }


    sealed class SignupState {
        object Loading : SignupState()
        data class Success(val user: FirebaseUser?) : SignupState()
        data class Error(val message: String) : SignupState()
    }





}