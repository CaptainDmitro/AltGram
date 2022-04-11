package org.captaindmitro.altgram.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import org.captaindmitro.altgram.databinding.FragmentLoginBinding
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpTextview.setOnClickListener {
            binding.userNameEditText.visibility = View.VISIBLE
            binding.signInTextview.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
            binding.registrationButton.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        binding.signInTextview.setOnClickListener {
            binding.userNameEditText.visibility = View.GONE
            binding.signUpTextview.visibility = View.VISIBLE
            binding.registrationButton.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        binding.loginButton.setOnClickListener {
            try {
                loginViewModel.signIn(
                    email = binding.emailEditText.editText?.text.toString(),
                    password = binding.passwordEditText.editText?.text.toString(),
                    onSuccess = {
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        it.findNavController().navigate(action)
                    },
                    onFailure = { Snackbar.make(it, "Error logging in", Snackbar.LENGTH_SHORT).show() }
                )
            } catch (e: Exception) {
                Snackbar.make(it, "$e", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.registrationButton.setOnClickListener {
            try {
                loginViewModel.signUp(
                    userName = binding.userNameEditText.editText?.text.toString(),
                    email = binding.emailEditText.editText?.text.toString(),
                    password = binding.passwordEditText.editText?.text.toString(),
                    onSuccess = {
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        it.findNavController().navigate(action)
                    },
                    onFailure = { Snackbar.make(it, "Error signing up", Snackbar.LENGTH_SHORT).show() }
                )
            } catch (e: Exception) {
                Snackbar.make(it, "$e", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}