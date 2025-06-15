package ru.otus.basicarchitecture.ui.summary

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.data.WizardCache
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(private val wizardCache: WizardCache): ViewModel() {
    var name = wizardCache.name
    var address = wizardCache.address
    var interests = wizardCache.interests
}