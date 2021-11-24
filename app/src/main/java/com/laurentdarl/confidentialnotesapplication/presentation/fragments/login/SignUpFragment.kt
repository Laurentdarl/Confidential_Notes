package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentSignUpBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.provider.MediaStore

import android.content.DialogInterface
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.presentation.fragments.AddNoteFragmentDirections
import com.laurentdarl.confidentialnotesapplication.utils.URIPathHelper
import java.util.*


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val uriPathHelper = URIPathHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)

        binding.tvUploadImg.setOnClickListener {
            openGalleryForImage()
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signUp() {
        val names = binding.tifNames.text.toString()
        val email = binding.tifEmail.text.toString()
        val password = binding.tifPassword.text.toString()
        val repeatPassword = binding.tifRepeatPassword.text.toString()
        validateInput(names, email, password, repeatPassword)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            binding.imgUpload.setImageURI(data?.data) // handle chosen image
        } else if (data == null || data.data == null ) {
            return
        }
    }

    private fun validateInput(names: String, email: String, password: String, repeatPassword: String) {

        when {
            TextUtils.isEmpty(names) -> Toast.makeText(requireContext(), "Please fill in your full names", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email) -> Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(requireContext(), "Please enter a password", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(repeatPassword) -> Toast.makeText(requireContext(), "Please confirm your password", Toast.LENGTH_SHORT).show()
            repeatPassword != password  -> Toast.makeText(requireContext(), "Passwords do not match, please check password", Toast.LENGTH_SHORT).show()
            else -> {

                Toast.makeText(requireContext(), "Note added successfully!", Toast.LENGTH_SHORT).show()
                val actions = SignUpFragmentDirections.actionSignUpFragmentToNotesFragment()
                findNavController().navigate(actions)
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }

}