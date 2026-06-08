package com.example.fonosapp.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseHelper {
    private const val SUPABASE_URL = "https://oevkfdkporlnubsjfsje.supabase.co"
    // Hãy thay thế bằng Anon Key (public) của bạn để app hoạt động
    private const val SUPABASE_KEY = "YOUR_SUPABASE_ANON_KEY_HERE"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
