package ru.otus.basicarchitecture.ui.address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.data.PersonAddress
import ru.otus.basicarchitecture.data.WizardCache
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val wizardCache: WizardCache): ViewModel() {

    var data = PersonAddress("", "", "")
    var showNext = MutableLiveData(false)

    private fun checkData() {
        showNext.value =
            data.country.isNotEmpty() && data.city.isNotEmpty() && data.address.isNotEmpty()
    }

    fun setCountry(country: String) {
        data = PersonAddress(country, data.city, data.address)
        checkData()
    }

    fun setCity(city: String) {
        data = PersonAddress(data.country, city, data.address)
        checkData()
    }

    fun setAddress(address: String) {
        data = PersonAddress(data.country, data.city, address)
        checkData()
    }

    fun getFromCache() {
        data = wizardCache.address
        checkData()
    }

    fun putToCache() {
        wizardCache.address = data
    }
}