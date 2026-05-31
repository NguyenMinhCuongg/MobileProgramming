package com.example.fonosapp

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoManager(private val onResult: (List<TodoItem>?, String?) -> Unit) {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun fetchTodos() {
        scope.launch {
            try {
                val items = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["todos"]
                        .select()
                        .decodeList<TodoItem>()
                }
                onResult(items, null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }
}
