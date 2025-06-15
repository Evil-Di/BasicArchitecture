package ru.otus.basicarchitecture.ui.name

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.data.PersonName
import ru.otus.basicarchitecture.data.WizardCache
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor(private val wizardCache: WizardCache): ViewModel() {

    var data = PersonName("", "", LocalDate.now())

    var showNext = MutableLiveData(false)
    var showFieldsEmpty = MutableLiveData(false)
    var showAgeRestricted = MutableLiveData(false)

    private fun checkAge(): Boolean = Period.between(data.birthDate, LocalDate.now()).years >= 18
    private fun checkName(): Boolean = data.name.isNotEmpty() && data.surName.isNotEmpty()

    private fun checkData() {
        val ageOk = checkAge()
        val nameOk = checkName()

        showNext.value = ageOk && nameOk
        showAgeRestricted.value = !ageOk && nameOk
        showFieldsEmpty.value = !nameOk
    }

    fun setName(name: String) {
        data = PersonName(name, data.surName, data.birthDate)
        checkData()
    }

    fun setSurName(surName: String) {
        data = PersonName(data.name, surName, data.birthDate)
        checkData()
    }

    fun setBirthDate(date: LocalDate) {
        data = PersonName(data.name, data.surName, date)
        checkData()
    }

    fun getFromCache() {
        data = wizardCache.name
        checkData()
    }

    fun putToCache() {
        wizardCache.name = data
    }
}
