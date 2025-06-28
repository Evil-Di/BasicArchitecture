package ru.otus.basicarchitecture.net

import java.io.IOException
import javax.inject.Inject

interface GetSuggestions {
    suspend operator fun invoke(query: String): Suggestions

    class Impl @Inject constructor(private val api: AddressSuggestionApi) : GetSuggestions {
        override suspend fun invoke(query: String): Suggestions {
            val response = api.getSuggestions(SuggestionQuery(query))
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            return response.body() ?: throw IOException("Empty body $response")
        }
    }
}