package com.example.boilerplate.di

import com.example.boilerplate.repository.authentication.AuthenticationRepository
import com.example.boilerplate.repository.authentication.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideAuthenticationRepositoryImpl(repo: AuthenticationRepositoryImpl): AuthenticationRepository
}