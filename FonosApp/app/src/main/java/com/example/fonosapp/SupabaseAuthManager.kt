package com.example.fonosapp

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupabaseAuthManager(private val onResult: (Boolean, String?) -> Unit) {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun signUp(email: String, pass: String) {
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    SupabaseHelper.client.auth.signUpWith(Email) {
                        this.email = email
                        this.password = pass
                    }
                }
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun signIn(email: String, pass: String) {
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    SupabaseHelper.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = pass
                    }
                }
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
