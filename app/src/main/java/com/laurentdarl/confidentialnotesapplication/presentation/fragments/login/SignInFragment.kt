package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signIn() {
        val email = binding.tifEmail.text.toString()
        val password = binding.tifPassword.text.toString()

        if (validateUser(email, password)) {
            val actions = SignInFragmentDirections.actionSignInFragmentToNotesFragment()
            findNavController().navigate(actions)
            Snackbar.make(binding.root, "Logged in successfully!", Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill all fields correctly", LENGTH_SHORT).show()
        }
    }

    private fun validateUser(email: String, password: String): Boolean {

        if (email.length < 5 && password.length < 5) {
            Toast.makeText(requireContext(), "email too short", LENGTH_SHORT).show()
        }
        return !(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }

}