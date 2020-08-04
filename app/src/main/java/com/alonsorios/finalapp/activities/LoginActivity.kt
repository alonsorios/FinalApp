package com.alonsorios.finalapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alonsorios.finalapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (isValidEmail(email) &&  isValidPassword(password)){
                logInByEmail(email, password)
            }else{
                toast("Please make sure all the data is correct")
            }
        }

        textViewForgotPassword.setOnClickListener{
            goToActivity<ForgoyPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

        buttonCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "Email no valido"
        }
        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "Password should contain 1 lowercase, 1 uppercase, 1 number, 1 special character an 4 characters length at least"
        }

    }

    private fun logInByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                if (mAuth.currentUser!!.isEmailVerified){
                    toast("User is now logged in")
                    goToActivity<MainActivity>(){
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }else{
                    toast("User most confirm email first")
                }
            }else{
                toast("And unexpected error occurred, please try again")
            }
        }
    }

}
