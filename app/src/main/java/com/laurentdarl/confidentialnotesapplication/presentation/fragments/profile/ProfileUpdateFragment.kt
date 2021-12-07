package com.laurentdarl.confidentialnotesapplication.presentation.fragments.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.data.models.User
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentProfileUpdateBinding
import com.laurentdarl.confidentialnotesapplication.presentation.fragments.login.SignUpFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileUpdateFragment : Fragment() {

    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser
    private lateinit var storageReference: StorageReference
    private val reference = FirebaseDatabase.getInstance().reference
    private lateinit var imageUri: Uri
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileUpdateBinding.inflate(layoutInflater)

        val genderArray = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_btn, genderArray)
        binding.tiGender.setAdapter(arrayAdapter)

        binding.btnUpdateProfile.setOnClickListener {
            val name = if (auth.currentUser != null) {
                auth.currentUser!!.displayName.toString()
            }else {
                ""
            }
            val phone = binding.tifUserPhone.text.toString()
            val gender = binding.tiGender.text.toString()
            val userName = binding.tifUserName.text.toString()
            val email = if (auth.currentUser != null) {
                auth.currentUser!!.email.toString()
            }else {
                ""
            }
            updateProfile(userName, name, phone, gender, email)
        }

        binding.selectImage.setOnClickListener {
            openGalleryForImage()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showProgressBar() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar() {
        dialog.dismiss()
    }

    private fun updateProfile(user_name: String, name: String, phone: String, gender: String, email: String) {
        //Change user_name
        if (!TextUtils.isEmpty(user_name)) {
            reference.child(getString(R.string.note_users))
                .child(auth.currentUser!!.uid)
                .child(getString(R.string.user_name))
                .setValue(user_name)
        }

        //Change phone_number
        if (!TextUtils.isEmpty(phone)) {
            reference.child(getString(R.string.note_users))
                .child(auth.currentUser!!.uid)
                .child(getString(R.string.phone_number))
                .setValue(phone)
        }

        //Change Gender
        if (TextUtils.isEmpty(gender)) {
            reference.child(getString(R.string.note_users))
                .child(auth.currentUser!!.uid)
                .child(getString(R.string.gender))
                .setValue(gender)
        }

        showProgressBar()
        val users = User(auth.currentUser!!.uid, user_name, name, phone, gender, email)
        val user1 = User("string", "userName", "name", "phone", "gender", "email")
        FirebaseDatabase.getInstance().reference
            .child(getString(R.string.note_users))
            .child(auth.currentUser!!.uid)
            .setValue(users)
            .addOnCompleteListener {
                hideProgressBar()
                val actions = ProfileUpdateFragmentDirections.actionProfileUpdateFragmentToProfileFragment()
                findNavController().navigate(actions)
            }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, SignUpFragment.REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SignUpFragment.REQUEST_CODE) {
            imageUri = data?.data!!
            uploadImageToFirebase(imageUri)
            binding.profileImageCard.setImageURI(imageUri) // handle chosen image
        } else if (data == null || data.data == null) {
            return
        }
    }

    private fun uploadImageToFirebase(image: Uri) {
        // compressing image
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, image)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

        val fileRef: StorageReference =
            storageReference.child("images/users/${auth.currentUser!!.uid}.jpg")
        fileRef.putBytes(reducedImage).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                //getting image url
//                binding.profileImageCard.setImageURI(it)
            }.addOnFailureListener {
                Log.i("Image DownLoad:", "Error getting image download url")
                Toast.makeText(
                    requireContext(),
                    "Profile image cannot be displayed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Profile image upload failed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setUserDetails(name: String, userImage: String = "") {
        user?.let {users ->
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(userImage))
                .build()
            users.updateProfile(profileUpdate).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Profile update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}