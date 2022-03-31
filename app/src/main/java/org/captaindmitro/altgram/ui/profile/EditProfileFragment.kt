package org.captaindmitro.altgram.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentEditProfileBinding
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newDisplayName = binding.name
        val email = binding.email
        val saveButton = binding.save
        val signOutButton = binding.signOut

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { loginViewModel.currentUser.collect { newDisplayName.setText(it?.displayName) } }
                launch { loginViewModel.currentUser.collect { email.setText(it?.email) } }
            }
        }

        saveButton.setOnClickListener {
            loginViewModel.changeDisplayName(
                newDisplayName.text.toString(),
                { findNavController().navigateUp() },
                { Snackbar.make(it, "Cannot update username. Try later.", Snackbar.LENGTH_SHORT).show() }
            )
        }

        signOutButton.setOnClickListener {
            loginViewModel.signOut()
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}