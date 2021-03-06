package com.example.boilerplate.repository.authentication

import com.example.boilerplate.di.NetworkClientDI
import com.example.boilerplate.model.User
import com.example.boilerplate.network.ApiResult
import com.example.boilerplate.network.DataSource
import com.example.boilerplate.network.NetworkClient
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(@NetworkClientDI private val client: DataSource) :
    AuthenticationRepository {
    override fun login(username: String, password: String) = flow<ApiResult<User?>> {
        emit(ApiResult.Loading(null, true))
        try {
            val response = client.login(username, password)
            emit(ApiResult.Success(response.user))
        } catch (e: Exception) {
            if (e is HttpException) {
                emit(ApiResult.Error(e.response()?.message()?.toString() ?: e.toString()))
            } else {
                emit(ApiResult.Error(e.message ?: e.toString()))
            }
        }
    }
}
