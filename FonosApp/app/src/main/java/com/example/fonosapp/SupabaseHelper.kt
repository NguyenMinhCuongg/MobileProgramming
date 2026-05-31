package com.example.fonosapp

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseHelper {
    // Thông tin đã được cập nhật từ cấu hình của bạn
    private const val SUPABASE_URL = "https://rrpgiosdylgjtetpeysf.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_zLJm4WDL6Qbhd5NnWwx8KA_Xtw46mB6"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
