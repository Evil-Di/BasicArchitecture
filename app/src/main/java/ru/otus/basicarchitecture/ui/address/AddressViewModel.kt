package ru.otus.basicarchitecture.ui.address

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.otus.basicarchitecture.data.WizardCache
import ru.otus.basicarchitecture.net.AddressSuggestionApi
import ru.otus.basicarchitecture.net.AuthInterceptor
import ru.otus.basicarchitecture.net.GetSuggestions
import ru.otus.basicarchitecture.net.NetService
import ru.otus.basicarchitecture.net.Suggestions
import ru.otus.basicarchitecture.net.buildRetrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    private val service: NetService
): ViewModel() {

    var data: String = ""
    var showNext = MutableLiveData(false)
    var suggestions = MutableLiveData<Suggestions?>(null)
    private var activeJob: Job? = null

    private fun checkData() {
        showNext.value = data.isNotEmpty()
    }

    fun setAddress(address: String) {
        data = address
        checkData()
    }

    fun getFromCache() {
        data = wizardCache.address
        checkData()
    }

    fun putToCache() {
        wizardCache.address = data
    }

    fun getSuggestions(edit: String) {
        if (edit != data) {
            activeJob = viewModelScope.launch {
                try {
                    suggestions.value = service.getSuggestions(edit)
                } catch (e: Exception) {
                    Log.e("address viewmodel getSuggestions Error: ", e.message.toString())
                }
            }
        }
        else {
            suggestions.value = null
        }
    }

    fun stopSuggestions() {
        activeJob?.cancel()
        activeJob = null
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {
    @Binds
    abstract fun netService(impl: NetService.Impl) : NetService

    @Binds
    abstract fun getSuggestions(impl: GetSuggestions.Impl) : GetSuggestions
}

@Module
@InstallIn(ViewModelComponent::class)
class MainModuleProvider {
    @Provides
    fun okHttp(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        })
        .build()

    @Provides
    fun retrofit(okHttp: OkHttpClient): Retrofit = buildRetrofit(okHttp)

    @Provides
    fun api(retrofit: Retrofit): AddressSuggestionApi =
        retrofit.create(AddressSuggestionApi::class.java)
}
