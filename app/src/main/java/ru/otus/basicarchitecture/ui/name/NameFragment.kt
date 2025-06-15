package ru.otus.basicarchitecture.ui.name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentNameBinding
import ru.otus.basicarchitecture.ui.FragmentBindingDelegate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class NameFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentNameBinding>(this)
    private val viewModel: NameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentNameBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.withBinding {
            viewModel.getFromCache()

            viewModel.showNext.observe(viewLifecycleOwner) {
                buttonNext.isEnabled = it
            }

            viewModel.showAgeRestricted.observe(viewLifecycleOwner) {
                txtRestrictedAccess.visibility =
                    if (it) View.VISIBLE else View.INVISIBLE
            }

            viewModel.showFieldsEmpty.observe(viewLifecycleOwner) {
                txtFillFields.visibility =
                    if (it) View.VISIBLE else View.INVISIBLE
            }

            nameInput.setText(viewModel.data.name)
            surnameInput.setText(viewModel.data.surName)
            dateInput.setText(formatDate(viewModel.data.birthDate))

            dateInput.setOnClickListener {
                pickDate(viewModel.data.birthDate) { newDate ->
                    dateInput.setText(formatDate(newDate))
                    viewModel.setBirthDate(newDate)
                }
            }

            nameInput.doAfterTextChanged { viewModel.setName(it.toString()) }
            surnameInput.doAfterTextChanged { viewModel.setSurName(it.toString()) }

            buttonNext.setOnClickListener {
                viewModel.putToCache()
                findNavController().navigate(R.id.action_to_address)
            }
        }
    }

    private fun formatDate(date: LocalDate): String = date.format(
        DateTimeFormatter.ofPattern(resources.getText(R.string.date_pattern).toString())
    )

    private fun pickDate(selDate: LocalDate, newDateHandler: (LocalDate) -> Unit ) {

        binding.withBinding {

            val calendar = Calendar.getInstance()
            calendar.set(selDate.year,selDate.monthValue - 1,selDate.dayOfMonth)

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(resources.getText(R.string.select_birthday))
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(calendar.timeInMillis)
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val newDate = Instant.ofEpochMilli(selectedDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                newDateHandler(newDate)
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
    }
}