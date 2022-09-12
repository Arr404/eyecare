package com.total.eyecare.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.total.eyecare.MainActivity
import com.total.eyecare.R
import com.total.eyecare.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var loginState:Boolean = true
    private lateinit var container: ConstraintLayout

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
           updateUiWithUser(currentUser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        container = binding.container
        val username = binding.username
        val password = binding.password
        val login = binding.buttonLogin
        val loading = binding.loading

        playAnimationFirst()

        binding.buttonSignup.setOnClickListener{
            if(loginState){
                binding.buttonLogin.text = "SIGN UP"
                binding.buttonSignup.text = "LOGIN"
                loginState = false
                playAnimationWhenLoginPressed()
            }else{
                binding.buttonLogin.text = "LOGIN"
                binding.buttonSignup.text = "SIGN UP"
                loginState = true
                playAnimationWhenSignUpPressed()
            }

        }
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            null
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE

                if(loginState) {
                    if (username.text.toString() != "" && password.text.toString() != "") {
                        auth.signInWithEmailAndPassword(
                            username.text.toString(),
                            password.text.toString()
                        )
                            .addOnCompleteListener(this@LoginActivity) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success")
                                    val user = auth.currentUser

                                    updateUiWithUser(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }
                        loading.visibility = View.GONE

                    }
                }else{
                    if (username.text.toString() != "" && password.text.toString() != ""){
                    auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
                            .addOnCompleteListener(this@LoginActivity) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser
                                    val profileUpdates = userProfileChangeRequest {
                                        displayName = binding.etLoginNama.text.toString()
                                        photoUri = Uri.parse("https://drive.google.com/file/d/1SEa3SLBzh3jjL_FTIyjZa6XjrYdjt711/view?usp=sharing")
                                    }
                                    user!!.updateProfile(profileUpdates)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(TAG, "User profile updated.")
                                                Toast.makeText(baseContext, "Account created.",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(baseContext, "Create User With Email failed.",
                                        Toast.LENGTH_SHORT).show()

                                }
                            }
                    loading.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun playAnimationWhenSignUpPressed(){
        val nama = ObjectAnimator.ofFloat(binding.etLoginNama, View.ALPHA, 0f).setDuration(500)
        val animLogin = ObjectAnimator.ofFloat(binding.buttonLogin, View.TRANSLATION_Y, 0f, -150f).setDuration(500)
        val animSignUp =
            ObjectAnimator.ofFloat(binding.buttonSignup, View.TRANSLATION_Y, 0f, -150f).setDuration(500)
        val animGoogle =
            ObjectAnimator.ofFloat(binding.buttonGoogle, View.TRANSLATION_Y, 0f, -150f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(animLogin,animGoogle,animSignUp)
        }

        AnimatorSet().apply {
            playSequentially(nama,together)
            start()
        }
    }
    private fun playAnimationWhenLoginPressed(){
        val nama = ObjectAnimator.ofFloat(binding.etLoginNama, View.ALPHA, 1f).setDuration(500)
        val animLogin = ObjectAnimator.ofFloat(binding.buttonLogin, View.TRANSLATION_Y, -150f, 0f).setDuration(500)
        val animSignUp =
            ObjectAnimator.ofFloat(binding.buttonSignup, View.TRANSLATION_Y, -150f, 0f).setDuration(500)
        val animGoogle =
            ObjectAnimator.ofFloat(binding.buttonGoogle, View.TRANSLATION_Y, -150f, 0f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(animLogin,animGoogle,animSignUp)
        }

        AnimatorSet().apply {
            playSequentially(nama,together)
            start()
        }
    }
    private fun playAnimationFirst() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(1000)
        val user = ObjectAnimator.ofFloat(binding.username, View.ALPHA, 1f).setDuration(1000)
        val pass = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(1000)
        val title = ObjectAnimator.ofFloat(binding.tvJudulSplash, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvPunch, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.buttonLogin,View.ALPHA,1f).setDuration(500)
        val buttonSignup = ObjectAnimator.ofFloat(binding.buttonSignup,View.ALPHA,1f).setDuration(500)
        val buttonGoogle = ObjectAnimator.ofFloat(binding.buttonGoogle,View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(user, pass,buttonLogin,buttonSignup,buttonGoogle)
        }


        AnimatorSet().apply {
            playSequentially(logo,title, desc, together)
            start()
        }
        ObjectAnimator.ofFloat(binding.tvJudulSplash, View.TRANSLATION_Y, 400f, -400f).apply {
            duration = 550
        }.start()
        ObjectAnimator.ofFloat(binding.tvPunch, View.TRANSLATION_Y, 400f, -400f).apply {
            duration = 550
        }.start()
        ObjectAnimator.ofFloat(binding.tvJudulSplash, View.TRANSLATION_X, -260f, 260f).apply {
            duration = 550
        }.start()
        ObjectAnimator.ofFloat(binding.tvPunch, View.TRANSLATION_X, -260f, 260f).apply {
            duration = 550
        }.start()
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, 160f, -160f).apply {
            duration = 1100
        }.start()

        ObjectAnimator.ofFloat(binding.buttonLogin, View.TRANSLATION_Y, 150f, -150f).apply {
            duration = 1100
        }.start()
        ObjectAnimator.ofFloat(binding.buttonSignup, View.TRANSLATION_Y, 150f, -150f).apply {
            duration = 1100
        }.start()
        ObjectAnimator.ofFloat(binding.buttonGoogle, View.TRANSLATION_Y, 150f, -150f).apply {
            duration = 1100
        }.start()
    }

    private fun updateUiWithUser(model: FirebaseUser?) {

        val welcome = getString(R.string.welcome)
        val displayName = model?.displayName
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
    private fun showError(message: String){
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show()
    }
}


/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}