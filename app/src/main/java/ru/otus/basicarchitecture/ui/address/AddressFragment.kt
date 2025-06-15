package ru.otus.basicarchitecture.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.ui.FragmentBindingDelegate

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentAddressBinding>(this)
    private val viewModel: AddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentAddressBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.withBinding {
            viewModel.getFromCache()

            viewModel.showNext.observe(viewLifecycleOwner) {
                txtFillFields.visibility = if (it) View.INVISIBLE else View.VISIBLE
                buttonNext.isEnabled = it
            }

            cityInput.setText(viewModel.data.city)
            cityInput.doAfterTextChanged { viewModel.setCity(it.toString()) }

            countryInput.setText(viewModel.data.country)
            countryInput.doAfterTextChanged { viewModel.setCountry(it.toString()) }

            addressInput.setText(viewModel.data.address)
            addressInput.doAfterTextChanged { viewModel.setAddress(it.toString()) }

            buttonNext.setOnClickListener {
                viewModel.putToCache()
                findNavController().navigate(R.id.action_to_interests)
            }
        }
    }
}