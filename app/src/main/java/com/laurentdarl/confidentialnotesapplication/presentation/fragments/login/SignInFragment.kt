package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.app.ProgressDialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null && !auth.currentUser!!.isAnonymous) {
           actions()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging into your account!")
        progressDialog.setCanceledOnTouchOutside(true)

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


        binding.tvAnonymous.setOnClickListener {
            anonymous()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signUp() {
        val actions = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(actions)
    }

    private fun signIn() {
        val email = binding.tifEmail.text.toString().trim {it <= ' '}
        val password = binding.tifPassword.text.toString().trim {it <= ' '}
        validateUser(email, password)
    }

    private fun anonymous() {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    actions()
                    Toast.makeText(requireContext(), "You've logged in anonymously!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
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
            !domainRestriction(email) -> {
                Toast.makeText(
                    requireContext(),
                    "Email must be a gmail account",
                    LENGTH_SHORT
                ).show()
            }
            else -> {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        progressDialog.show()
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            if (auth.currentUser!!.isEmailVerified) {
                                Snackbar.make(binding.root, "Logged in successfully!", Snackbar.LENGTH_SHORT).show()
                                actions()
                            } else {
                                Toast.makeText(requireContext(), "Please check your Email inbox for a verification link",
                                    Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(requireContext(), task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun actions() {
        val actions = SignInFragmentDirections.actionSignInFragmentToNotesFragment()
        findNavController().navigate(actions)
    }

    private fun domainRestriction(email: String): Boolean {
        val domain = email.substring(email.indexOf("@") + 1).toLowerCase()
        return domain == DOMAIN_NAME
    }

    private fun resendVerification() {

    }

    companion object {
        const val DOMAIN_NAME = "gmail.com"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}