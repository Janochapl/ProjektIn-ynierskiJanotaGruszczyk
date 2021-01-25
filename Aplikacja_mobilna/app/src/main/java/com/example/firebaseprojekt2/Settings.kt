package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.TextViewCompat
import org.jetbrains.anko.startActivityForResult
import java.lang.Exception

lateinit var textViewChangePassword: TextView
lateinit var textViewChangeEmail: TextView

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        textViewChangePassword = findViewById(R.id.changePassword)
        textViewChangeEmail = findViewById(R.id.changeEmail)

        textViewChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivityForResult(intent, 1)
        }

        textViewChangeEmail.setOnClickListener {
            val intent = Intent(this, ChangeEmail::class.java)
            startActivityForResult(intent, 1)
        }


    }
}