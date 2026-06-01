package com.example.fonosapp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    @SerialName("name_vi") val nameVi: String,
    @SerialName("name_en") val nameEn: String,
    @SerialName("icon_url") val iconUrl: String? = null,
    @SerialName("gradient_colors") val gradientColors: List<String>? = null
)

@Serializable
data class Author(
    val id: String,
    val name: String,
    val biography: String? = null
)

@Serializable
data class Book(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
    val price: Long = 0,
    @SerialName("is_audiobook") val isAudiobook: Boolean = true,
    @SerialName("is_ebook") val isEbook: Boolean = false,
    @SerialName("author_id") val authorId: String? = null,
    @SerialName("publisher_id") val publisherId: String? = null,
    @SerialName("published_year") val publishedYear: Int? = null
)

@Serializable
data class BookCategoryRelation(
    @SerialName("book_id") val bookId: String,
    @SerialName("category_id") val categoryId: String
)

@Serializable
data class BookInCategory(
    val books: Book
)
