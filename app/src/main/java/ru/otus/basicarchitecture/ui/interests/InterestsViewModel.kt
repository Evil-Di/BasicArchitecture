package ru.otus.basicarchitecture.ui.interests

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.data.WizardCache
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(private val wizardCache: WizardCache): ViewModel() {

    var data: Map<String, Boolean> = wizardCache.interests

    fun getFromCache() {
        data = wizardCache.interests
    }

    fun putToCache() {
        wizardCache.interests = data
    }
}