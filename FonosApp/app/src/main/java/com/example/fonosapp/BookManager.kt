package com.example.fonosapp

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

    /**
     * Lấy danh sách sách theo ID thể loại
     */
    fun fetchBooksByCategory(categoryId: String, onResult: (List<Book>?, String?) -> Unit) {
        scope.launch {
            try {
                val books = withContext(Dispatchers.IO) {
                    // Sử dụng join để lấy thông tin sách từ bảng book_categories
                    SupabaseHelper.client.postgrest["book_categories"]
                        .select(io.github.jan.supabase.postgrest.query.Columns.raw("*, books(*)")) {
                            filter {
                                eq("category_id", categoryId)
                            }
                        }
                        .decodeList<BookInCategory>()
                        .map { it.books }
                }
                onResult(books, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    /**
     * Lấy thông tin chi tiết của một cuốn sách theo ID
     */
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

    /**
     * Lấy URL công khai của ảnh bìa sách từ Storage.
     * @param path: Tên file hoặc đường dẫn trong bucket (ví dụ: "covers/book1.jpg")
     */
    fun getBookCoverUrl(path: String): String {
        return SupabaseHelper.client.storage.from("books").publicUrl(path)
    }
}
