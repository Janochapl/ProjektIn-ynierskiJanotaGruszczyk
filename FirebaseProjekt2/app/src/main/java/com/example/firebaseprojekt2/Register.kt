package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.tasks.OnCompleteListener

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
        val emailET: EditText = findViewById(R.id.editTextRLogin);
        val passwordET: EditText = findViewById(R.id.editTextRPassword);
        val confirmPasswordET: EditText = findViewById((R.id.editTextRCPassword));
        val progressBar: ProgressBar = findViewById(R.id.progressBar);
        progressBar.visibility = View.INVISIBLE;
        buttonRegister.setOnClickListener{

            if(TextUtils.isEmpty(emailET.text.toString())){
                emailET.setError("Email is required.")
                return@setOnClickListener;
            }
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString()).matches()) {
                emailET.setError("Email is incorrect.")
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(passwordET.text.toString())){
                passwordET.setError("Password is required.")
                return@setOnClickListener;
            }
            if(passwordET.text.toString().length < 6){
                passwordET.setError("Password must have more than 5 characters.")
                return@setOnClickListener;
            }
            if(passwordET.text.toString()!=confirmPasswordET.text.toString()){
                confirmPasswordET.setError("Both passwords must be identical.")
                return@setOnClickListener;
            }

            progressBar.visibility = View.VISIBLE;

            mAuth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString()).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                mAuth.currentUser!!.sendEmailVerification()
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User created.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    intent.putExtra("FROM_REGISTER", "True")
                    startActivity(intent);
                } else {
                    Toast.makeText(applicationContext, "Error! " + task.exception, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val toLogin: TextView = findViewById(R.id.textView2)
        toLogin.setOnClickListener{
            val intent = Intent(this, Login::class.java);
            startActivity(intent);
        }
        val toMainPage: TextView = findViewById(R.id.textView6)
        toMainPage.setOnClickListener{
            val intent = Intent(this, MainPage::class.java);
            startActivity(intent);
        }
    }
}
