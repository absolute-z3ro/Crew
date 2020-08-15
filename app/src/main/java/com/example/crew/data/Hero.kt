package com.example.crew.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hero(val name: String, val imageUrl: String = "")