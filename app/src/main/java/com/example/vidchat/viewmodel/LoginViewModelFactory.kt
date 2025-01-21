

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vidchat.Repository.AuthRepository
import com.example.vidchat.viewmodel.LoginViewModel


class LoginViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
