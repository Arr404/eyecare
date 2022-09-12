package com.total.eyecare.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun logout() {
        Firebase.auth.signOut()
    }
}