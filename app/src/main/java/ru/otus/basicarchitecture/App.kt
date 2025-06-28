package ru.otus.basicarchitecture

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import ru.otus.basicarchitecture.net.SessionManager
import javax.inject.Singleton

@HiltAndroidApp
class App : Application()

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun sessionManager(impl: SessionManager.Impl): SessionManager
}