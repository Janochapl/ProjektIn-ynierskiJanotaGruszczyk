package com.example.firebaseprojekt2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseprojekt2.adapters.RecyclerAdapter
import com.example.firebaseprojekt2.data.Product
import com.example.firebaseprojekt2.functions.initializeMenu
import com.example.firebaseprojekt2.functions.itemsFunctions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main_activity.*
import java.util.*
import kotlin.Exception


class MainActivity : AppCompatActivity(), Observer/*, NavigationView.OnNavigationItemSelectedListener*/ {


    lateinit var adapter: RecyclerAdapter
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var test: TextView
    lateinit var products: String
    lateinit var productsObjects: Product
    lateinit var category: String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        try{
            products = intent.getStringExtra("products")
            println(products)
            category = intent.getStringExtra("category")
            var gson = Gson()
            productsObjects = gson?.fromJson(products, Product::class.java)
            //print("MAINACTIVITY")
            //print(productsObjects)
        }catch (e: Exception){
            print(e)
            Toast.makeText(this, "Unable to find product", Toast.LENGTH_SHORT)
            products = ""
        }

        try{
            adapter = RecyclerAdapter(productsObjects, this, category)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
        }catch(e: Exception) {
            print("PROBOWALEM ZALADOWAC MAIN ACTIVITY")
        }

    }

    override fun onBackPressed() {
        Toast.makeText(this, "BACK PRESSED", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
        finishAffinity()
        super.onBackPressed()
    }

    override fun update(p0: Observable?, p1: Any?) {
        val itemList = productsObjects
        adapter = RecyclerAdapter(itemList, this, category)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }


}
