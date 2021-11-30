package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentResetPasswordBinding
import com.laurentdarl.confidentialnotesapplication.presentation.fragments.UpdateNoteFragmentArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser
    private val args by navArgs<ResetPasswordFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater)

        binding.tifEmail.setText(args.email)

        binding.btnPasswordReset.setOnClickListener {
           
        }

        // Inflate the layout for this fragment
        return binding.root
    }

//    private fun resetPassword() {
//
//        auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val actions =
//                        ResetPasswordFragmentDirections.actionResetPasswordFragmentToSignInFragment()
//                    findNavController().navigate(actions)
//                    Toast.makeText(
//                        requireContext(),
//                        "A password reset link has been sent to your email address",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        "Password reset failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//    }
}