package com.example.firebaseprojekt2.functions

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.firebaseprojekt2.DataModel.FavoriteModel
import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.data.DatabaseProductItem
import com.example.firebaseprojekt2.data.DatabaseStorageProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

fun isUserLogged(): Boolean{
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    return currentUser!=null
}

fun getUserEmail(): String?{
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    return mAuth.currentUser?.email
}

fun getCurrentUser(): FirebaseUser?{
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    return mAuth.currentUser
}

fun changePassword(newPassword: String): Boolean{
    var currentUser =  getCurrentUser()
    return if(currentUser!=null){
        currentUser.updatePassword(newPassword)
        true
    }else{
        false
    }
}

fun addItemToDatabaseStorage(
    product: DatabaseStorageProduct,
    product_name: String,
    product_type: String,
    context: Context){
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    try{
        if(currentUser!=null){
            val myRef = database.reference
                .child("database")
                .child(product_type).child(product_name)
            myRef.setValue(product)
        }else{
            Log.e("DATABASE",
                "Error occurred: any user logged in.")
        }
    }catch (e: Exception){
        Log.e("DATABASE ERROR",
            e.toString())
    }
}

fun addItemToLastSearched(
    product: DatabaseProductItem,
    context: Context) {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    try{
        if(currentUser!=null){
            val myRef = database.getReference()
                .child("users")
                .child(currentUser)
                .child("LastSearched")
                .child(product.product_name)
            myRef.setValue(product)
        }else{
            Log.e("DATABASE",
                "Error occurred: any user logged in.")
        }
    }catch (e: Exception){
        Log.e("DATABASE ERROR",
            e.toString())
    }
}

fun deleteItemFromLastSearched(
    productName: String,
    context: Context){
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    try{
        if(currentUser!=null){
            val myRef = database.reference
                .child("users")
                .child(currentUser)
                .child("LastSearched")
                .child(productName)
            myRef.removeValue()
        }else{
            Toast.makeText(context,
                "Error occurred: any user logged in.",
                Toast.LENGTH_SHORT).show()
        }
    }catch (e: Exception){
        Log.e("DATABASE ERROR",
            e.toString())
    }
}

fun addItemToFavorite(
    product: DatabaseProductItem,
    context: Context) {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    try{
        if(currentUser!=null){
            val myRef = database.getReference()
                .child("users")
                .child(currentUser)
                .child("Favorite")
                .child(product.product_name)
            myRef.setValue(product)
        }else{
            Log.e("DATABASE",
                "Error occurred: any user logged in.")
        }
    }catch(e: Exception){
        Log.e("DATABASE ERROR",
            e.toString())
    }
}

fun deleteItemFromFavorite(
    productName: String,
    context: Context){
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    try{
        if(currentUser!=null){
            val myRef = database.reference
                .child("users")
                .child(currentUser)
                .child("Favorite")
                .child(productName)
            myRef.removeValue()
        }else{
            Log.e("DATABASE",
                "Error occurred: any user logged in.")
        }
    }catch (e: Exception){
        Log.e("DATABASE ERROR",
            e.toString())
    }
}

fun getDatabaseRef(child: String): DatabaseReference? {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val currentUser: String? = mAuth.currentUser?.uid
    if(currentUser!=null)
    {
        return FirebaseDatabase.getInstance().reference
            .child("users")
            .child(currentUser)
            .child(child)
    }else{
        println("Error occured. Any user logged in.")
        return null
    }
}