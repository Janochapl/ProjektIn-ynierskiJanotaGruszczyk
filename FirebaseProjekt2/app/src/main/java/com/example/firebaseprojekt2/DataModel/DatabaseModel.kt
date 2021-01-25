package com.example.firebaseprojekt2.DataModel

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.firebaseprojekt2.MainActivity
import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.data.DatabaseProductItem
import com.example.firebaseprojekt2.data.DatabaseStorageProduct
import com.example.firebaseprojekt2.functions.getDatabaseRef
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*

class DatabaseModel(productName: String, productType: String): Observable() {

    private var mValueDataListener: ValueEventListener? = null
    private var mProduct: DatabaseStorageProduct = DatabaseStorageProduct()
    init{

        val database = FirebaseDatabase.getInstance()
        val databaseReference =
            database.reference.child("database").child(productType).child(productName)
        if (mValueDataListener != null){
            databaseReference?.removeEventListener(mValueDataListener!!)
        }
        mValueDataListener = null
        mValueDataListener = object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try{
                    val data: DatabaseStorageProduct = DatabaseStorageProduct()
                    if (dataSnapshot.exists()){
                        for(snapshot: DataSnapshot in dataSnapshot.children){
                            try{
                                data.updateData(snapshot)
                            } catch(e: Exception){
                                println(e)
                            }
                        }
                        mProduct = data
                    }
                }catch (e: Exception){
                    mProduct.json = ""
                    println(e)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

                if(p0!=null){
                    println(p0)
                }
            }
        }
        databaseReference?.addValueEventListener(mValueDataListener!!)
    }

    fun getData(): DatabaseStorageProduct {
        return mProduct
    }


}