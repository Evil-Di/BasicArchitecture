package ru.otus.basicarchitecture.data

import java.time.LocalDate

data class PersonName(
    val name: String,
    val surName: String,
    val birthDate: LocalDate
)

data class PersonAddress(
    val country: String,
    val city: String,
    val address: String
)
