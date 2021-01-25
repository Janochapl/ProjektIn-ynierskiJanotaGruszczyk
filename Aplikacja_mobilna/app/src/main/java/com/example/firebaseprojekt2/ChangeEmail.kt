package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.firebaseprojekt2.functions.getCurrentUser
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
lateinit var editTextCurrentEmail: EditText
lateinit var editTextCurrentPasswordE: EditText
lateinit var  editTextNewEmail: EditText
lateinit var buttonChangeEmail: Button
lateinit var progressBarE: ProgressBar
class ChangeEmail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        editTextCurrentEmail=findViewById(R.id.currentEmail)
        editTextCurrentPasswordE=findViewById(R.id.currentPassword)
        editTextNewEmail=findViewById(R.id.newEmail)
        buttonChangeEmail=findViewById(R.id.buttonChangeEmail)
        progressBarE=findViewById(R.id.progressBarE)
        buttonChangeEmail.setOnClickListener {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextNewEmail.text).matches()) {
                editTextNewEmail.error = "Email is incorrect."
                return@setOnClickListener;
            }
            if(!isPasswordMatch()){
                editTextCurrentEmail.error= "Wrong e-mail/password"
                editTextCurrentPasswordE.error = "Wrong e-mail/password"
                return@setOnClickListener;
            }else{
                try{
                    getCurrentUser()!!.updateEmail(editTextNewEmail.text.toString())
                    Toast.makeText(this, "E-mail changed successful", Toast.LENGTH_SHORT).show()
                    finish()
                }catch(e: Exception){
                    Toast.makeText(this, "Something went wrong. Please again later.", Toast.LENGTH_SHORT).show()
                    println(e)
                }
            }
        }
    }
    private fun isPasswordMatch(): Boolean{
        var currentUser = getCurrentUser()
        if(currentUser!=null){
            var email = currentUser.email
            if(email!=null){
                var credential: AuthCredential = EmailAuthProvider.getCredential(
                    editTextCurrentEmail.text.toString(),
                    editTextCurrentPasswordE.text.toString())
                return try{
                    getCurrentUser()!!.reauthenticate(credential)
                    true
                }catch (e: Exception){
                    print(e)
                    false
                }

            }
        }
        return false
    }
}
