package ru.otus.basicarchitecture.ui.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.ui.InterestChips
import ru.otus.basicarchitecture.databinding.FragmentInterestsBinding
import ru.otus.basicarchitecture.ui.FragmentBindingDelegate

@AndroidEntryPoint
class InterestsFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentInterestsBinding>(this)
    private val viewModel: InterestsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentInterestsBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.withBinding {
            viewModel.getFromCache()

            InterestChips.load(interestsChips, viewModel.data)

            buttonNext.setOnClickListener {
                val map = mutableMapOf<String, Boolean>()
                interestsChips.children.toList().filterIsInstance<Chip>().map {
                    map.put(it.text.toString(), it.isChecked)
                }
                viewModel.data = map
                viewModel.putToCache()
                findNavController().navigate(R.id.action_to_summary)
            }
        }
    }
}