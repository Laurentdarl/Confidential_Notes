package com.laurentdarl.confidentialnotesapplication.presentation.fragments.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
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
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.presentation.fragments.AddNoteFragmentDirections
import com.laurentdarl.confidentialnotesapplication.utils.URIPathHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.util.*


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var uriPathHelper = URIPathHelper()
    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null && !auth.currentUser!!.isAnonymous) {
            homeActions()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Registration in progress!")
        progressDialog.setCanceledOnTouchOutside(true)

        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)

        val genderArray = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_btn, genderArray)
        binding.tiGender.setAdapter(arrayAdapter)

        binding.tvUploadImg.setOnClickListener {
            openGalleryForImage()
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.tvAnonymous.setOnClickListener {

            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        homeActions()
                        Toast.makeText(requireContext(), "You've logged in anonymously!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signUp() {
        val names = binding.tifNames.text.toString()
        val email = binding.tifEmail.text.toString().trim {it <= ' '}
        val password = binding.tifPassword.text.toString().trim {it <= ' '}
        val repeatPassword = binding.tifRepeatPassword.text.toString().trim {it <= ' '}
        val images = Glide.with(this).load(binding.imgUpload).into(binding.imgUpload)

        validateInput(names, email, password, repeatPassword)
//        setUserDetails(names)

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data
            binding.imgUpload.setImageURI(imageUri) // handle chosen image

            uploadImageToFirebase()
        }
        else if (data == null || data.data == null) {
            return
        }
    }

    private fun uploadImageToFirebase() {

    }

    private fun validateInput(
        names: String,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        when {
            TextUtils.isEmpty(names) -> Toast.makeText(
                requireContext(),
                "Please fill in your full names",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(
                requireContext(),
                "Please enter a valid email address",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                requireContext(),
                "Please enter a password",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(repeatPassword) -> Toast.makeText(
                requireContext(),
                "Please confirm your password",
                Toast.LENGTH_SHORT
            ).show()
            repeatPassword != password -> Toast.makeText(
                requireContext(),
                "Passwords do not match, please check password",
                Toast.LENGTH_SHORT
            ).show()
            email.length <= 6 -> {
                Toast.makeText(
                    requireContext(),
                    "Email should exceed 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            password.length <= 6 -> {
                Toast.makeText(
                    requireContext(),
                    "Password should exceed 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            !domainRestriction(email) -> {
                Toast.makeText(
                    requireContext(),
                    "Email must be a gmail account",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        progressDialog.show()
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "Registration successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            sendVerificationEmail()
                            auth.signOut()
                            loginActions()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireContext(),
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }

    private fun homeActions() {
        val actions = SignUpFragmentDirections.actionSignUpFragmentToNotesFragment()
        findNavController().navigate(actions)
    }

    private fun loginActions() {
        val actions = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        findNavController().navigate(actions)
    }

    private fun domainRestriction(email: String): Boolean {
        val domain = email.substring(email.indexOf("@") + 1).toLowerCase()
        return domain == DOMAIN_NAME
    }

    private fun sendVerificationEmail() {
        val firebaseUser: FirebaseUser = auth.currentUser!!
        firebaseUser.sendEmailVerification()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Email verification has been sent to the email address provided",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not verify the email address provided",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun setUserDetails(name: String, userImage: String = "", phoneNumber: Int = 0) {
        user?.let {users ->
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(userImage))
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    users.updateProfile(profileUpdate).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Profile updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 100
        const val DOMAIN_NAME = "gmail.com"
        const val RC_SIGN_IN = 15
        const val User_Id = "user_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}