package com.laurentdarl.confidentialnotesapplication.data.models

import androidx.room.PrimaryKey

data class User(
    val id: Int,
    val user_name: String,
    val phone: String,
    val email: String,
    val image: String,
)
