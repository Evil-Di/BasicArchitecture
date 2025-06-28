package ru.otus.basicarchitecture.net

import ru.otus.basicarchitecture.BuildConfig
import javax.inject.Inject

interface SessionManager {
    fun getToken(): String

    class Impl @Inject constructor() : SessionManager {
        override fun getToken(): String = BuildConfig.daDataApiKey
    }
}
