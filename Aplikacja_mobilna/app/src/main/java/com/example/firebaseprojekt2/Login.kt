package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.tasks.OnCompleteListener

class Login : AppCompatActivity() {

    lateinit var fromRegister: String
    lateinit var verifyEmailTV: TextView
    lateinit var resendButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        verifyEmailTV = findViewById(R.id.verifyEmailTV)
        resendButton = findViewById(R.id.buttonResendVerification)
        try{
            fromRegister = intent.getStringExtra("FROM_REGISTER")
            verifyEmailTV.visibility = View.VISIBLE
            resendButton.visibility=View.VISIBLE
        }catch (e: Exception){
            fromRegister = "False"
        }
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
        val progressBar: ProgressBar = findViewById(R.id.progressBar);
        progressBar.visibility = View.INVISIBLE;
        ///////////////////
        buttonLogin.setOnClickListener {
            verifyEmailTV.visibility = View.INVISIBLE
            resendButton.visibility=View.INVISIBLE
            val emailET: EditText = findViewById(R.id.editTextLogin)
            val passwordET: EditText = findViewById(R.id.editTextPassword)
            progressBar.visibility = View.VISIBLE;
            if(TextUtils.isEmpty(emailET.text.toString())){
                emailET.setError("Type your email.");
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(passwordET.text.toString())){
                passwordET.setError("Type your password.");
                return@setOnClickListener;
            }

            mAuth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString()).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    if(mAuth.currentUser!!.isEmailVerified){
                        Toast.makeText(applicationContext, "Successfully logged in.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainPage::class.java);
                        startActivity(intent);
                        this.finish()
                    }else{
                        verifyEmailTV.visibility = View.VISIBLE
                        resendButton.visibility=View.VISIBLE
                        progressBar.visibility = View.INVISIBLE;
                    }

                } else {
                    Toast.makeText(applicationContext, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE;
                }
            })

        }
        buttonResendVerification.setOnClickListener {
            mAuth.currentUser?.sendEmailVerification()
        }


        val toRegister: TextView = findViewById(R.id.textView4)
        toRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java);
            startActivity(intent);
            this.finish()
        }
        val toMainPage: TextView = findViewById(R.id.textView6)
        toMainPage.setOnClickListener{
            val intent = Intent(this, MainPage::class.java);
            startActivity(intent);
            this.finish()
        }
    }
}
