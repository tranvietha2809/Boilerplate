package com.example.boilerplate.network

import com.example.boilerplate.BuildConfig
import com.example.boilerplate.model.User
import com.example.boilerplate.model.request.LoginRequest
import com.example.boilerplate.model.response.LoginResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitNetwork {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

@Singleton
class NetworkClient @Inject constructor(json: Json) : DataSource {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    } else {
                        setLevel(HttpLoggingInterceptor.Level.NONE)
                    }
                }
            )
            .build()
    ).addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
        .create(RetrofitNetwork::class.java)

    override suspend fun login(username: String, password: String): LoginResponse {
        return client.login(LoginRequest(username, password))
    }
}