package org.captaindmitro.altgram.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.databinding.FragmentLoginBinding
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registrationButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var signInTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNameEditText = binding.userNameEditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        loginButton = binding.loginButton
        registrationButton = binding.registrationButton
        signUpTextView = binding.signUpTextview
        signInTextView = binding.signInTextview


        signUpTextView.setOnClickListener {
            userNameEditText.visibility = View.VISIBLE
            signInTextView.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            registrationButton.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        signInTextView.setOnClickListener {
            userNameEditText.visibility = View.GONE
            signUpTextView.visibility = View.VISIBLE
            registrationButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        loginButton.setOnClickListener {
            try {
                loginViewModel.signIn(
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString(),
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

        registrationButton.setOnClickListener {
            try {
                loginViewModel.signUp(
                    userName = userNameEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString(),
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