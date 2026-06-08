package com.example.fonosapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("is_premium") val isPremium: Boolean = false,
    @SerialName("books_read_count") val booksReadCount: Int = 0,
    @SerialName("listening_hours") val listeningHours: Double = 0.0
)

@Serializable
data class Author(
    val id: String,
    val name: String,
    val biography: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null
)

@Serializable
data class Category(
    val id: String,
    @SerialName("name_vi") val nameVi: String,
    @SerialName("name_en") val nameEn: String,
    @SerialName("icon_url") val iconUrl: String? = null,
    @SerialName("gradient_colors") val gradientColors: List<String>? = null
)

@Serializable
data class Book(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
    @SerialName("banner_url") val bannerUrl: String? = null,
    val price: Double = 0.0,
    @SerialName("is_audiobook") val isAudiobook: Boolean = true,
    @SerialName("is_ebook") val isEbook: Boolean = true,
    @SerialName("author_id") val authorId: String? = null,
    @SerialName("category_id") val categoryId: String? = null,
    @SerialName("duration_seconds") val durationSeconds: Int? = null
)

@Serializable
data class BookChapter(
    val id: String,
    @SerialName("book_id") val bookId: String,
    val title: String? = null,
    @SerialName("chapter_index") val chapterIndex: Int,
    @SerialName("start_time_seconds") val startTimeSeconds: Int? = null,
    @SerialName("end_time_seconds") val endTimeSeconds: Int? = null
)

@Serializable
data class BookInCategory(
    val books: Book
)
