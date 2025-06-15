package ru.otus.basicarchitecture.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.LocalDate

private val interestsMap = mapOf(
    Pair("Sports",          false),
    Pair("Music",           false),
    Pair("Fine Arts",       false),
    Pair("Books",           false),
    Pair("Travels",         false),
    Pair("Dancing",         false),
    Pair("Computer Games",  false),
    Pair("Food",            false),
    Pair("Pets",            false)
)

class WizardCache {
    var name: PersonName = PersonName("", "", LocalDate.now())
    var address: PersonAddress = PersonAddress("", "", "")
    var interests: Map<String, Boolean> = interestsMap
}


@Module
@InstallIn(ActivityRetainedComponent::class)
object WizardCacheModule {
    @Provides
    @ActivityRetainedScoped
    fun wizardCache(): WizardCache = WizardCache()
}