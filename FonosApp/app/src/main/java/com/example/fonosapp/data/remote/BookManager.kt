package com.example.fonosapp.data.remote

import com.example.fonosapp.data.models.Book
import com.example.fonosapp.data.models.BookChapter
import com.example.fonosapp.data.models.Category
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookManager {
    private val scope = CoroutineScope(Dispatchers.Main)

    fun fetchCategories(onResult: (List<Category>?, String?) -> Unit) {
        scope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["categories"]
                        .select()
                        .decodeList<Category>()
                }
                onResult(categories, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun fetchBooks(onResult: (List<Book>?, String?) -> Unit) {
        scope.launch {
            try {
                val books = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["books"]
                        .select()
                        .decodeList<Book>()
                }
                onResult(books, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun fetchBooksByCategory(categoryId: String, onResult: (List<Book>?, String?) -> Unit) {
        scope.launch {
            try {
                val books = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["books"]
                        .select {
                            filter {
                                eq("category_id", categoryId)
                            }
                        }
                        .decodeList<Book>()
                }
                onResult(books, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun fetchBookById(bookId: String, onResult: (Book?, String?) -> Unit) {
        scope.launch {
            try {
                val book = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["books"]
                        .select {
                            filter {
                                eq("id", bookId)
                            }
                        }
                        .decodeSingle<Book>()
                }
                onResult(book, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun fetchChapters(bookId: String, onResult: (List<BookChapter>?, String?) -> Unit) {
        scope.launch {
            try {
                val chapters = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["book_chapters"]
                        .select {
                            filter {
                                eq("book_id", bookId)
                            }
                        }
                        .decodeList<BookChapter>()
                }
                onResult(chapters, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun getBookCoverUrl(path: String): String {
        return SupabaseHelper.client.storage.from("books").publicUrl(path)
    }
}
