package com.example.boilerplate.repository.authentication

import com.example.boilerplate.model.User
import com.example.boilerplate.network.ApiResult
import com.example.boilerplate.network.NetworkClient
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(private val client: NetworkClient) : AuthenticationRepository {
    override suspend fun login(username: String, password: String) = flow<ApiResult<User?>> {
        emit(ApiResult.Loading(null, true))
        try {
            val response = client.login(username, password)
            emit(ApiResult.Loading(null, false))
            emit(ApiResult.Success(response.user))
        } catch (e : Exception) {
            emit(ApiResult.Error(e.message ?: e.toString()))
        }
    }
}
