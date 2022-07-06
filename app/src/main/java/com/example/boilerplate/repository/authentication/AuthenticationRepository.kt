package com.example.boilerplate.repository.authentication

import com.example.boilerplate.model.User
import com.example.boilerplate.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(username: String, password: String) : Flow<ApiResult<User?>>
}