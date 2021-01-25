package com.example.firebaseprojekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.firebaseprojekt2.DataModel.DatabaseModel
import com.example.firebaseprojekt2.data.DatabaseStorageProduct
import com.example.firebaseprojekt2.functions.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import productDownload

class MainPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, productDownload.AsyncResponse {
    lateinit var editTextProductName: EditText
    lateinit var imageViewElectronic: ImageView
    lateinit var imageViewLiterature: ImageView
    lateinit var imageViewFashion: ImageView
    lateinit var imageViewHouse: ImageView
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var category: String
    lateinit var currentUser: String
    lateinit var mAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var actualCategory: String
    lateinit var actualCategoryView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


        //DECLARATION OF LAYOUT ELEMENTS
        editTextProductName = findViewById(R.id.editTextSearch)
        imageViewElectronic = findViewById(R.id.imageViewElectronic)
        imageViewLiterature = findViewById(R.id.imageViewLiterature)
        imageViewFashion = findViewById(R.id.imageViewFashion)
        imageViewHouse = findViewById(R.id.imageViewHouse)
        progressBar = findViewById(R.id.progressBarMain)
        imageViewElectronic.clearColorFilter()
        imageViewLiterature.clearColorFilter()
        imageViewFashion.clearColorFilter()
        imageViewHouse.clearColorFilter()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        toggle.setHomeAsUpIndicator(R.drawable.ic_favorite)
        category = ""



        //Authenticator
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser?.email.toString()
        if(isUserLogged()){
            if(!mAuth.currentUser!!.isEmailVerified)
                mAuth.signOut()
        }
        initializeMenu(navView, this)



        //LISTENERS{

        imageViewElectronic.setOnClickListener{

            startSearching("electronic", imageViewElectronic)
        }

        imageViewLiterature.setOnClickListener{

            startSearching("literature", imageViewLiterature)
        }

        imageViewFashion.setOnClickListener{

            startSearching("fashion", imageViewFashion)
        }

        imageViewHouse.setOnClickListener{

            startSearching("house", imageViewHouse)

        }
    }


    private fun startSearching(categ: String, imgV: ImageView) {
        println("ROZPOCZYNAM SZUKANIE")
        actualCategory = categ
        actualCategoryView = imgV

        progressBar.visibility= View.VISIBLE
        if (editTextProductName.text.length < 3) {
            editTextProductName.setError("Product name must have at least 3 characters.")
            progressBar.visibility= View.INVISIBLE
            return
        } else {
            imageViewElectronic.clearColorFilter()
            imageViewLiterature.clearColorFilter()
            imageViewFashion.clearColorFilter()
            imageViewHouse.clearColorFilter()
            category = ""
            imgV.setColorFilter(resources.getColor(R.color.colorPrimary))
            category = when (categ) {
                "electronic" -> "1"
                "fashion" -> "2"
                "house" -> "3"
                "literature" -> "4"
                else -> {
                    Toast.makeText(this, "Something went really wrong...", Toast.LENGTH_SHORT).show()
                    return
                }
            }
                var temp: String =
                    "http://192.168.0.248/items-list/" + category + "/" + editTextProductName.text
                productDownload(this).execute(temp)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        itemsFunctions(item, this, this)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun processFinish(output: String?) {
        if(output==null){
            Toast.makeText(this,
                "Unfortunately we didn't found your product. Try again or consider add a keyword before product name?",
                Toast.LENGTH_LONG).show()
            progressBar.visibility=View.GONE
        }else if(output=="EXCEPTION"){
                startSearching(actualCategory, actualCategoryView)
        }else if(output=="CONNECTION"){
            progressBar.visibility= View.GONE
            Toast.makeText(this,
                "Unable to connect. Please check your internet connection.",
                Toast.LENGTH_LONG).show()
        }
        else{
            val intent = Intent(this, MainActivity::class.java);
            intent.putExtra("products", output)
            intent.putExtra("title", editTextProductName.text)
            intent.putExtra("category", category)
            progressBar.visibility= View.GONE
            startActivity(intent)

        }

    }

}