package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.firebaseprojekt2.functions.getCurrentUser
import com.example.firebaseprojekt2.functions.getUserEmail
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.security.KeyStore

lateinit var editTextCurrentPassword: EditText
lateinit var editTextNewPassword: EditText
lateinit var editTextConfirmedNewPassword: EditText
lateinit var buttonChangePassword: Button
lateinit var progressBar: ProgressBar
class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        editTextCurrentPassword=findViewById(R.id.currentPassword)
        editTextNewPassword=findViewById(R.id.newPassword)
        editTextConfirmedNewPassword=findViewById(R.id.confirmNewPassword)
        buttonChangePassword=findViewById(R.id.buttonChangePassword)
        progressBar=findViewById(R.id.progressBar)
        buttonChangePassword.setOnClickListener {
            if(editTextNewPassword.text.toString().length<6){
                editTextNewPassword.error = "New password must have at least 6 characters"
                return@setOnClickListener
            }
            if(editTextNewPassword.text.toString()!= editTextConfirmedNewPassword.text.toString()){
                editTextNewPassword.error = "Passwords don't match"
                editTextConfirmedNewPassword.error = "Passwords don't match"
                return@setOnClickListener
            }else if(!isPasswordMatch()){
                editTextCurrentPassword.error = "Wrong password."
            }else{
                try{
                    getCurrentUser()!!.updatePassword(editTextNewPassword.text.toString())
                    Toast.makeText(this, "Password changed successful", Toast.LENGTH_SHORT).show()
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
                    email,
                    editTextCurrentPassword.text.toString())
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