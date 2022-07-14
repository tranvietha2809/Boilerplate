package com.example.boilerplate.di

import com.example.boilerplate.network.DataSource
import com.example.boilerplate.network.NetworkClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object {
        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }
    }
    @Binds
    @NetworkClientDI
    fun providesNetworkClient(client: NetworkClient) : DataSource
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkClientDI