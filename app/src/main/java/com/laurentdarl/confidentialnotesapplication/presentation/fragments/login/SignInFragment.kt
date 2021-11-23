package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater)

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.tvSignUp.setOnClickListener {
            signUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signUp() {
        val actions = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(actions)
    }

    private fun signIn() {
        val email = binding.tifEmail.text.toString()
        val password = binding.tifPassword.text.toString()
        validateUser(email, password)
    }

    private fun validateUser(email: String, password: String) {
        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(
                    requireContext(),
                    "Email field is empty",
                    LENGTH_SHORT
                ).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(
                    requireContext(),
                    "Password field is empty",
                    LENGTH_SHORT
                ).show()
            }
            email.length <= 6 -> {
                Toast.makeText(
                    requireContext(),
                    "Email should exceed 6 characters",
                    LENGTH_SHORT
                ).show()
            }
            password.length <= 6 -> {
                Toast.makeText(
                    requireContext(),
                    "Password should exceed 6 characters",
                    LENGTH_SHORT
                ).show()
            }
            else -> {
                val actions = SignInFragmentDirections.actionSignInFragmentToNotesFragment()
                findNavController().navigate(actions)
                Snackbar.make(binding.root, "Logged in successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}