@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.boilerplate.repository_test

import com.example.boilerplate.model.User
import com.example.boilerplate.model.response.LoginResponse
import com.example.boilerplate.network.ApiResult
import com.example.boilerplate.network.DataSource
import com.example.boilerplate.repository.authentication.AuthenticationRepository
import com.example.boilerplate.repository.authentication.AuthenticationRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthenticationRepositoryTest {

    lateinit var authenticationRepository: AuthenticationRepository
    lateinit var client: DataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        client = mockk()
        authenticationRepository = AuthenticationRepositoryImpl(client)
    }

    @Test
    fun `Login Successfully with username and password`() = runTest {
        coEvery {
            client.login("username", "password")
        } returns LoginResponse(User("1", "username", "name", "role"), "message")
        val flow = authenticationRepository.login("username", "password")
        Assert.assertEquals(
            flow.toList(),
            listOf(
                ApiResult.Loading(null, true),
                ApiResult.Success(User("1", "username", "name", "role"))
            )
        )
    }

    @Test
    fun `Login Fails with HttpException`() = runTest {
        val responseBody = byteArrayOf().toResponseBody()
        coEvery {
            client.login("", "")
        } throws HttpException(Response.error<ResponseBody>(403, responseBody))
        val flow = authenticationRepository.login("", "")
        Assert.assertEquals(
            flow.toList(),
            listOf(
                ApiResult.Loading(null, true),
                ApiResult.Error(
                    HttpException(
                        Response.error<ResponseBody>(
                            403,
                            responseBody
                        )
                    ).response()!!.message().toString()
                )
            )
        )
    }

    @Test
    fun `Login Fails with other exceptions`() = runTest {
        val exception = Exception()
        coEvery {
            client.login("", "")
        } throws exception
        val flow = authenticationRepository.login("", "")
        Assert.assertEquals(
            flow.toList(),
            listOf(
                ApiResult.Loading(null, true),
                ApiResult.Error(
                    exception.toString()
                )
            )
        )
    }
}