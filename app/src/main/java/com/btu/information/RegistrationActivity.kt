package com.btu.information

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()

        signUpBtn.setOnClickListener {
            signUpUser()
        }

        backBtn.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signUpUser() {
        if(user_email.text.toString().isEmpty()){
            user_email.error = "Please enter email!"
            user_email.requestFocus()
            return

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(user_email.text.toString()).matches()) {
            user_email.error = "Please enter valid email!"
            user_email.requestFocus()
            return
        }

        if(user_password.text.toString().isEmpty()){
            user_password.error = "Please enter password!"
            user_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(user_email.text.toString(), user_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext, "Email sent.",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}
