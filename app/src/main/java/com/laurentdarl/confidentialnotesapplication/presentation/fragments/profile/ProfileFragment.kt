package com.laurentdarl.confidentialnotesapplication.presentation.fragments.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.data.models.User
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentProfileBinding
import com.laurentdarl.confidentialnotesapplication.presentation.fragments.login.SignUpFragment

import android.graphics.Bitmap
import android.view.Window
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser
    private lateinit var storageReference: StorageReference
    private val reference = FirebaseDatabase.getInstance().reference
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.editProfile.setOnClickListener {
            val actions = ProfileFragmentDirections.actionProfileFragmentToProfileUpdateFragment()
            findNavController().navigate(actions)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun userDetails() {
        if (user != null) {
            val uid = user!!.uid
            val name = user!!.displayName
            val email = user!!.email
            val photoUrl = user!!.photoUrl

        }
    }

    // Getting User data from FireBase Database

    private fun getUserDetails() {
        // First Query Method
        val query = reference.child(getString(R.string.note_users))
            .orderByKey()
            .equalTo(auth.currentUser!!.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.getValue(User::class.java)
                binding.userName.text = children!!.user_name
                binding.tvProfileEmail.text = children.email
                binding.tvProfilePhone.text = children.phone
                binding.tvProfileGender.text = children.gender
//                Glide.with(this@ProfileFragment).load(children.).into(binding.profileImage)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("PROFILE FRAGMENT", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun displayProfileImage() {
        val pathRef = storageReference.child("images/users/${auth.currentUser!!.uid}.jpg")
//        val gsReference = storageReference.storage.getReferenceFromUrl("gs://bucket/images/users/${auth.currentUser!!.uid}.jpg")
//        val httpsReference = storageReference.storage.getReferenceFromUrl(
//            "https://firebasestorage.googleapis.com/b/bucket/o/images%20users%20${auth.currentUser!!.uid}.jpg")

        pathRef.downloadUrl.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }
}