package com.example.boilerplate.model.response

import com.example.boilerplate.model.User
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(val user: User)