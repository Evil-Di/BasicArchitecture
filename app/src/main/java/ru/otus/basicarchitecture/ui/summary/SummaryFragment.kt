package ru.otus.basicarchitecture.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentSummaryBinding
import ru.otus.basicarchitecture.ui.FragmentBindingDelegate
import ru.otus.basicarchitecture.ui.InterestChips
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentSummaryBinding>(this)
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentSummaryBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.withBinding {
            name.text = viewModel.name.name
            surname.text = viewModel.name.surName
            birthDate.text = formatDate(viewModel.name.birthDate)

            address.text = String.format(resources.getString(R.string.address_mask),
                viewModel.address.country,
                viewModel.address.city,
                viewModel.address.address)

            InterestChips.load(interestsChips, viewModel.interests) { isClickable = false }
        }
    }

    private fun formatDate(date: LocalDate): String = date.format(
        DateTimeFormatter.ofPattern(resources.getText(R.string.date_pattern).toString())
    )
}