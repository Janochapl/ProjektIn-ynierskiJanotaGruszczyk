package com.example.firebaseprojekt2.data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.time.LocalDateTime
import kotlin.Exception

class Product : ArrayList<ProductItem>()

data class ProductItem(
    val img_url: String,
    val info_text: String,
    val product_name: String
)

class ProductWithImages : ArrayList<ProductWithImagesItem>()

data class ProductWithImagesItem(
    val img: Bitmap,
    val info_text: String,
    val product_name: String
)

class ProductWithPrice : ArrayList<ProductWithPriceItem>()

data class ProductWithPriceItem(
    val img_url: String,
    val p_price: String,
    val p_url: String,
    val product_name: String
)

class ProductWithImagesWithPrice : ArrayList<ProductWithImagesWithPriceItem>()

data class ProductWithImagesWithPriceItem(
    val img: Bitmap,
    val p_price: String,
    val p_url: String,
    val product_name: String
)

class DatabaseStorageProduct{
    lateinit var date: String
    var json: String
    init{
        json = "ERROR"
    }
    @SuppressLint("NewApi")
    fun updateData(snapshot: DataSnapshot){
        try{
            var temp = snapshot.value.toString()
            json = temp.substring(1)
            date = LocalDateTime.now().toString()
        }catch (e: Exception){
            Log.e("DB_STORAGE_PRODUCT", e.toString())
        }
    }
}

class DatabaseProduct() : ArrayList<DatabaseProductItem>(){

    constructor(list: List<DatabaseProductItem>): this(){
        list.forEach{
            this.add(it)
        }
    }
    fun toProduct(): Product {
        var tempList: Product = Product()
        this.forEach {
            tempList.add(ProductItem(
                it.img_url,
                it.info_text,
                it.product_name))
        }
        return tempList
    }
}

class DatabaseProductItem{
    lateinit var id: String
    lateinit var product_name: String
    lateinit var info_text: String
    lateinit var img_url: String
    lateinit var category: String
    constructor(id: String,
                product_name: String,
                info_text: String,
                img_url: String,
                category: String){
        this.id = id
        this.product_name = product_name
        this.info_text = info_text
        this.img_url = img_url
        this.category = category
    }
    constructor(snapshot: DataSnapshot){
        try{
            val data: HashMap<String, Any> =
                snapshot.value as HashMap<String, Any>
            id = data["id"] as String
            product_name = data["product_name"] as String
            info_text = data["info_text"] as String
            img_url = data["img_url"] as String
            category = data["category"] as String
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun toProductItem(): ProductItem {
        return ProductItem(this.img_url, this.info_text, this.product_name)
    }
}



