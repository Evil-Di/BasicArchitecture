package ru.otus.basicarchitecture.ui.address

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.net.Suggestion
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
            addressInput.setText(viewModel.data)
            addressInput.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(editable: Editable?) {
                    viewModel.getSuggestions(editable.toString())
                }

            })
            addressInput.doAfterTextChanged {
                viewModel.setAddress(it.toString())
            }

            addressInput.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    addressInput.dismissDropDown()
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    return@OnEditorActionListener true // Focus will do whatever you put in the logic.
                }
                false // Focus will change according to the actionId
            })

            viewModel.suggestions.observe(viewLifecycleOwner) {
                if (it != null) {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        it.suggestions.map(Suggestion::value)
                    )
                    addressInput.setAdapter(adapter)
                    addressInput.popupElevation
                    if (addressInput.applicationWindowToken != null) {
                        addressInput.showDropDown()
                    }
                }
                else {
                    addressInput.dismissDropDown()
                }
            }

            viewModel.showNext.observe(viewLifecycleOwner) {
                txtFillFields.visibility = if (it) View.INVISIBLE else View.VISIBLE
                buttonNext.isEnabled = it
            }

            buttonNext.setOnClickListener {
                viewModel.putToCache()
                findNavController().navigate(R.id.action_to_interests)
            }
        }
    }

    override fun onDestroyView() {
        viewModel.stopSuggestions()
        super.onDestroyView()
    }
}