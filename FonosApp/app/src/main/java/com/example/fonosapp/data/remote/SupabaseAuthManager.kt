package com.example.fonosapp.data.remote

import com.example.fonosapp.data.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
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

    fun fetchUserProfile(userId: String, onProfileResult: (Profile?, String?) -> Unit) {
        scope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    SupabaseHelper.client.postgrest["profiles"]
                        .select {
                            filter {
                                eq("id", userId)
                            }
                        }
                        .decodeSingle<Profile>()
                }
                onProfileResult(profile, null)
            } catch (e: Exception) {
                onProfileResult(null, e.message)
            }
        }
    }
}
