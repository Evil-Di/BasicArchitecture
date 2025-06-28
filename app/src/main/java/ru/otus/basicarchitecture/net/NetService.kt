package ru.otus.basicarchitecture.net

import javax.inject.Inject

interface NetService {
    suspend fun getSuggestions(query: String): Suggestions

    class Impl @Inject constructor(
        private val getSuggestionCommand: GetSuggestions,
    ) : NetService {
        override suspend fun getSuggestions(query: String): Suggestions = getSuggestionCommand(query)
    }
}