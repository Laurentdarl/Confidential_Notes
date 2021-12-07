package com.laurentdarl.confidentialnotesapplication.data.models

import androidx.room.PrimaryKey

data class User(
    var id: String? = null,
    var user_name: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var gender: String? = null,
    var email: String? = null
)
