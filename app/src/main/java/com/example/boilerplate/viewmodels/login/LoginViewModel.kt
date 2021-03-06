package com.example.boilerplate.viewmodels.login

import androidx.lifecycle.ViewModel
import com.example.boilerplate.network.ApiStatus
import com.example.boilerplate.repository.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserLoggedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    suspend fun doLogin(username: String, password: String) {
        authenticationRepository.login(username, password).collect {
            when (it.status) {
                ApiStatus.ERROR -> {
                    _uiState.emit(LoginUiState(errorMessage = it.message))
                }
                ApiStatus.LOADING -> {
                    _uiState.emit(LoginUiState(isLoading = true))
                }
                ApiStatus.SUCCESS -> {
                    _uiState.emit(LoginUiState(isUserLoggedIn = true))
                }
            }
        }
    }
}