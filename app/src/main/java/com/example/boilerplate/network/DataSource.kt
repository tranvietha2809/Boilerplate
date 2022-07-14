package com.example.boilerplate.network

import com.example.boilerplate.model.response.LoginResponse

interface DataSource {
    suspend fun login(username : String, password : String) : LoginResponse
}