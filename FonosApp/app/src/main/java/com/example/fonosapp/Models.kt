package com.example.fonosapp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    @SerialName("name_vi") val nameVi: String,
    @SerialName("name_en") val nameEn: String,
    @SerialName("icon_url") val iconUrl: String? = null
)

@Serializable
data class Author(
    val id: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String? = null
)

@Serializable
data class Book(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
    @SerialName("rating_avg") val ratingAvg: Double = 0.0,
    @SerialName("author_id") val authorId: String? = null,
    @SerialName("is_audiobook") val isAudiobook: Boolean = true
)
