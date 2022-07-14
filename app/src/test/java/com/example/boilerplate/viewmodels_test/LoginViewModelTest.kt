package com.example.boilerplate.viewmodels_test

import com.example.boilerplate.model.User
import com.example.boilerplate.network.ApiResult
import com.example.boilerplate.repository.authentication.AuthenticationRepository
import com.example.boilerplate.viewmodels.login.LoginUiState
import com.example.boilerplate.viewmodels.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class LoginViewModelTest {

    lateinit var viewModel: LoginViewModel
    lateinit var authenticationRepository: AuthenticationRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        authenticationRepository = mockk()
        viewModel = LoginViewModel(authenticationRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UiState is LoginUiState(isUserLoggedIn = true) when login successfully `() =
        runTest {
            val flow = flow {
                emit(ApiResult.Loading(null, isLoading = true))
                emit(ApiResult.Success(User("1", "username", "name", "role")))
            }
            every {
                authenticationRepository.login("", "")
            } returns flow

            val result = mutableListOf<LoginUiState>()
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.uiState.toList(result)
            }
            viewModel.doLogin("", "")
            Assert.assertEquals(3, result.size)
            Assert.assertEquals(LoginUiState(isUserLoggedIn = true), result.last())
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UiState is LoginUiState(isLoading = true) when logging in `() =
        runTest {
            val flow = flow {
                emit(ApiResult.Loading(null, isLoading = true))
            }
            every {
                authenticationRepository.login("", "")
            } returns flow

            val result = mutableListOf<LoginUiState>()
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.uiState.toList(result)
            }
            viewModel.doLogin("", "")
            Assert.assertEquals(2, result.size)
            Assert.assertEquals(LoginUiState(isLoading = true), result.last())
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UiState is LoginUiState(errorMessage) when logging in `() =
        runTest {
            val flow = flow {
                emit(ApiResult.Error("Error"))
            }
            every {
                authenticationRepository.login("", "")
            } returns flow

            val result = mutableListOf<LoginUiState>()
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.uiState.toList(result)
            }
            viewModel.doLogin("", "")
            Assert.assertEquals(2, result.size)
            Assert.assertEquals(LoginUiState(errorMessage = "Error"), result.last())
            job.cancel()
        }
}