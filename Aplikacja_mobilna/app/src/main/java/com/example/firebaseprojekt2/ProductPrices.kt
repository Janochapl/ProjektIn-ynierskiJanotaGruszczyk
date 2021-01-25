package com.example.firebaseprojekt2

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseprojekt2.adapters.RecyclerAdapterPrices
import com.example.firebaseprojekt2.data.DatabaseProductItem
import com.example.firebaseprojekt2.data.ProductWithPrice
import com.example.firebaseprojekt2.functions.addItemToFavorite
import com.example.firebaseprojekt2.functions.deleteItemFromFavorite
import com.example.firebaseprojekt2.functions.initializeMenu
import com.example.firebaseprojekt2.functions.itemsFunctions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main_activity.recyclerView
import kotlinx.android.synthetic.main.content_prices.*
import productDownload
import java.time.LocalDateTime
import kotlin.Exception

class ProductPrices : AppCompatActivity()/*,NavigationView.OnNavigationItemSelectedListener*/, productDownload.AsyncResponse{
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var textViewTitle: TextView
    lateinit var textViewInfo: TextView
    lateinit var imageViewProduct: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var productTitle: String
    lateinit var productInfo: String
    lateinit var productImage: Bitmap
    lateinit var category: String
    lateinit var img_url: String
    lateinit var products: ProductWithPrice
    lateinit var adapter: RecyclerAdapterPrices
    lateinit var smileLayout: RelativeLayout
    lateinit var sadLayout: RelativeLayout
    lateinit var favoriteButton: MenuItem
    lateinit var productAPIAddress: String
    var isFavorite = false
    var mValueDataListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_prices)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle=findViewById(R.id.titleMainProduct)
        textViewInfo=findViewById(R.id.infoMainProduct)
        imageViewProduct=findViewById(R.id.imageMainProduct)
        progressBar=findViewById(R.id.progressBarPrice)
        smileLayout=findViewById(R.id.smileWaitingScreen)
        sadLayout=findViewById(R.id.sadWaitingScreen)

        //EXTRAS
        productTitle=intent.getStringExtra("product_name")
        productInfo=intent.getStringExtra("info_text")
        productImage=intent.getParcelableExtra("image")
        category=intent.getStringExtra("category")
        img_url=intent.getStringExtra("img_url")
        progressBar.visibility=View.VISIBLE
        try{
            checkIfFavorite()
        }catch (e: Exception){
            Log.e("DATABASE ERROR", e.toString())
        }

        //SETTING DEFAULT
        textViewTitle.text=productTitle
        textViewInfo.text=productInfo
        val canvas: Canvas = Canvas(productImage)
        imageViewProduct.setImageBitmap(productImage)
        //RUNNING PRODUCTS PRICES DOWNLOAD
        productAPIAddress = "http://192.168.0.248/items/" + category + "/" + productTitle
        productDownload(this).execute(productAPIAddress)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        favoriteButton=menu!!.findItem(R.id.favorite)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(isFavorite){
            menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite)
        }else{
            menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_nonfavorite)
        }



        return super.onPrepareOptionsMenu(menu)
    }

    @SuppressLint("NewApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite ->{
                if(isFavorite){
                    item.setIcon(R.drawable.ic_nonfavorite)
                    deleteItemFromFavorite(productTitle, this)
                    isFavorite = false
                }else{
                    item.setIcon(R.drawable.ic_favorite)
                    var tempProduct: DatabaseProductItem = DatabaseProductItem(LocalDateTime.now().toString(), productTitle, productInfo, img_url, category)
                    addItemToFavorite(tempProduct, this)
                    isFavorite = true

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDatabaseRef(): DatabaseReference? {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
        val currentUser: String? = mAuth.currentUser?.uid
        if(currentUser!=null)
        {
            return FirebaseDatabase.getInstance().reference
                .child("users").child(currentUser).child("Favorite").child(productTitle)
        }else{
            println("Error occured. Any user logged in.")
            return null
        }
    }

    private fun checkIfFavorite(){
            if (mValueDataListener != null){
                getDatabaseRef()
                    ?.removeEventListener(mValueDataListener!!)
            }
            mValueDataListener = null
            mValueDataListener = object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try{
                        if (dataSnapshot.exists()){
                            isFavorite = true
                            favoriteButton.setIcon(R.drawable.ic_favorite)
                        }else{
                            isFavorite = false
                            favoriteButton.setIcon(R.drawable.ic_nonfavorite)
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
            getDatabaseRef()
                ?.addListenerForSingleValueEvent(mValueDataListener!!)

    }


    override fun processFinish(output: String?) {

        if(output==null){
            progressBar.visibility=View.GONE
            smileWaitingScreen.visibility = View.GONE
            sadLayout.visibility = View.VISIBLE
            Toast.makeText(this, "We didn't find any product offers", Toast.LENGTH_SHORT).show()
        }else if(output=="EXCEPTION"){
            productDownload(this).execute(productAPIAddress)
        }else if(output=="CONNECTION"){
            progressBar.visibility= View.GONE
            Toast.makeText(this,
                "Unable to connect. Please check your internet connection.",
                Toast.LENGTH_LONG).show()
        }else{
            var gson = Gson()
            products = gson?.fromJson(output, ProductWithPrice::class.java)
            adapter = RecyclerAdapterPrices(products, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            progressBar.visibility=View.GONE
            smileLayout.visibility=View.GONE
            recyclerView.visibility=View.VISIBLE
        }

    }
}