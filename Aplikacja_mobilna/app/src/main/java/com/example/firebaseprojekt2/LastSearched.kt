package com.example.firebaseprojekt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseprojekt2.DataModel.LastSearchedModel
import com.example.firebaseprojekt2.adapters.RecyclerAdapterDatabase
import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.functions.initializeMenu
import com.example.firebaseprojekt2.functions.itemsFunctions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_main_activity.*
import java.util.*

class LastSearched : AppCompatActivity(), Observer/*, NavigationView.OnNavigationItemSelectedListener */{
    lateinit var mAuth: FirebaseAuth
    lateinit var currentUser: String
    lateinit var products: DatabaseProduct
    lateinit var adapter: RecyclerAdapterDatabase
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Authenticator
        setTitle("Last Searched")
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser?.email.toString()
        products = loadData()
        val sortedList = products.sortedWith(compareByDescending { it.id })
        val sortedProducts = DatabaseProduct(sortedList)
        adapter = RecyclerAdapterDatabase(sortedProducts, this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun loadData(): DatabaseProduct {
        LastSearchedModel
        LastSearchedModel.addObserver(this)
        val rawData = LastSearchedModel.getData()
        return rawData
    }

    override fun update(p0: Observable?, p1: Any?) {
        products = loadData()
        val sortedList = products.sortedWith(compareByDescending { it.id })
        val sortedProducts = DatabaseProduct(sortedList)
        adapter = RecyclerAdapterDatabase(sortedProducts, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

}