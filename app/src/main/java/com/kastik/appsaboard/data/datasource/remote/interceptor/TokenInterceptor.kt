package com.kastik.appsaboard.data.datasource.remote.interceptor

import com.kastik.appsaboard.data.datasource.local.AuthenticationLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class TokenInterceptor @Inject constructor(
    private val localDataSource: AuthenticationLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { localDataSource.getAboardAccessToken() }
        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else chain.request()
        return chain.proceed(newRequest)
    }
}