package com.example.boilerplate.network

import com.example.boilerplate.model.response.LoginResponse

interface ExampleDataSource {
    suspend fun login(username : String, password : String) : LoginResponse
}