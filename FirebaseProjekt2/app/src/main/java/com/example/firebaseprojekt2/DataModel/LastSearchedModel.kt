package com.example.firebaseprojekt2.DataModel

import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.data.DatabaseProductItem
import com.example.firebaseprojekt2.functions.getDatabaseRef
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*

object LastSearchedModel: Observable() {

    private var mValueDataListener: ValueEventListener? = null
    private var mLastSearchedList: DatabaseProduct = DatabaseProduct()

    init{
        if (mValueDataListener != null){
            getDatabaseRef("LastSearched")?.removeEventListener(mValueDataListener!!)
        }
        mValueDataListener = null
        mValueDataListener = object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try{
                    val data: DatabaseProduct = DatabaseProduct()
                    if (dataSnapshot != null){
                        for(snapshot: DataSnapshot in dataSnapshot.children){
                            try{
                                data.add(DatabaseProductItem(snapshot))
                            } catch(e: Exception){
                                println(e)
                            }
                        }
                        mLastSearchedList = data
                        setChanged()
                        notifyObservers()
                    }
                }catch (e: Exception){
                    println(e)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

                if(p0!=null){
                    println(p0)
                }
            }
        }
        getDatabaseRef("LastSearched")?.addValueEventListener(mValueDataListener!!)
    }

    fun getData(): DatabaseProduct {
        return mLastSearchedList
    }


}