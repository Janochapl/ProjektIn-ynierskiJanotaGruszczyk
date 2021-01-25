package com.example.firebaseprojekt2.functions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import com.example.firebaseprojekt2.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header.view.*

fun initializeMenu(navView: NavigationView, context: Context){

    if(isUserLogged()){
        try{
            navView.menu.findItem(R.id.nav_logout).title = "Sign Out"
            navView.menu.findItem(R.id.nav_favorite).isVisible = true
            navView.menu.findItem(R.id.nav_lastSearched).isVisible = true
            navView.menu.findItem(R.id.nav_update).isVisible = true
            var whoLogged: String = "Logged as " + getUserEmail()
            var header = navView.getHeaderView(0)
            header.textViewWhoLogged.text = whoLogged

        }catch(e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }else{
        navView.menu.findItem(R.id.nav_favorite).isVisible = false
        navView.menu.findItem(R.id.nav_lastSearched).isVisible = false
        navView.menu.findItem(R.id.nav_update).isVisible = false
    }
}

fun itemsFunctions(item: MenuItem, context: Context, activity: Activity){
    when (item.itemId) {
        R.id.nav_search -> {
            if (context !is MainPage) {
                val intent = Intent(context, MainPage::class.java);
                context.startActivity(intent)
            }
        }
        R.id.nav_favorite -> {
            if (context !is Favorite){
                if(isUserLogged()){
                    val intent = Intent(context, Favorite::class.java)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(context, "Please login to use this feature.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        R.id.nav_lastSearched -> {
            if (context !is LastSearched){
                if(isUserLogged()){
                    val intent = Intent(context, LastSearched::class.java)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(context, "Please login to use this feature.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        R.id.nav_update -> {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
            if(mAuth.currentUser!=null){
                val intent = Intent(context, Settings::class.java)
                context.startActivity(intent)

            }else{
                Toast.makeText(context, "Something went really wrong...", Toast.LENGTH_SHORT)
            }
        }
        R.id.nav_logout -> {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
            if(mAuth.currentUser!=null){
                mAuth.signOut()
                item.title = "Sign In"

            }else{
                val intent = Intent(context, Login::class.java)
                context.startActivity(intent)
                activity.finishAffinity()
            }
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)
            activity.finishAffinity()
        }
        else -> {
            print("Something went really wrong...")
        }
    }
}