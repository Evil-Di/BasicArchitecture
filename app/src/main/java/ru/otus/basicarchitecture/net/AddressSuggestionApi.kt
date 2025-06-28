package ru.otus.basicarchitecture.net

import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

private const val baseUrl = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/"

@Serializable data class SuggestionQuery(val query:String)
@Serializable data class Suggestion(val value:String)
@Serializable data class Suggestions(val suggestions: List<Suggestion>)

interface AddressSuggestionApi {
    @POST("address")
    suspend fun getSuggestions(@Body query: SuggestionQuery): Response<Suggestions>
}

fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
