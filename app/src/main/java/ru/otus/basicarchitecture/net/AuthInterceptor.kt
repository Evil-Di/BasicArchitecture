package ru.otus.basicarchitecture.net

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestWithAuth = request.newBuilder()
            .header("Authorization", "Token ${sessionManager.getToken()}")
            .build()

        return chain.proceed(requestWithAuth)
    }
}