package com.example.firebaseprojekt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseprojekt2.DataModel.FavoriteModel
import com.example.firebaseprojekt2.adapters.RecyclerAdapterDatabase
import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.functions.initializeMenu
import com.example.firebaseprojekt2.functions.itemsFunctions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_main_activity.*
import java.util.*

class Favorite : AppCompatActivity(), Observer/*, NavigationView.OnNavigationItemSelectedListener*/ {
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
        //ADAPTER
        setTitle("Favorite")
        products = loadData()
        adapter = RecyclerAdapterDatabase(products, this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun loadData(): DatabaseProduct {
        FavoriteModel
        FavoriteModel.addObserver(this)
        val rawData = FavoriteModel.getData()
        return rawData
    }

    override fun update(p0: Observable?, p1: Any?) {
        products = loadData()
        adapter = RecyclerAdapterDatabase(products, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

}