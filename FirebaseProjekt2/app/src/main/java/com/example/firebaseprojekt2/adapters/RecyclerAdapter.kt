package com.example.firebaseprojekt2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseprojekt2.data.DatabaseProductItem
import com.example.firebaseprojekt2.data.Product
import com.example.firebaseprojekt2.ProductPrices
import com.example.firebaseprojekt2.R
import com.example.firebaseprojekt2.functions.DownloadImageTask
import com.example.firebaseprojekt2.functions.addItemToLastSearched
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class RecyclerAdapter(private val itemList: Product, val context: Context, val category: String): /*Filterable,*/ RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item,
        parent, false)
        return ViewHolder(itemView)
    }


    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = itemList[position]
        try{
            var tempURL: String = currentItem.img_url
            DownloadImageTask(holder.imageView).execute(tempURL)
            holder.imgUrl.text=tempURL
        }catch (e: Exception) {
            Log.e("DOWNLOADING IMAGE", e.toString())
        }

        holder.textTitle.text = currentItem.product_name
        var tempInfoText = currentItem.info_text
        tempInfoText =  tempInfoText.replace(';', '\n')
        holder.textInfo.text = tempInfoText
        holder.imageView.isClickable

        holder.imageView.setOnClickListener{
            println(holder.adapterPosition)
        }

        holder.itemView.setOnClickListener{

            var compressedBitmap = it.imageView.drawToBitmap()
            val product = DatabaseProductItem(LocalDateTime.now().toString(), it.textTitle.text.toString(), it.textInfo.text.toString(), it.img_url.text.toString(), category)
            try{
                addItemToLastSearched(product, context)
            }catch (e: Exception){
                Log.e("DATABASE ERROR", e.toString())
            }

            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
            val intent = Intent(context, ProductPrices::class.java).apply {

                putExtra("product_name", it.textTitle.text)
                putExtra("info_text", it.textInfo.text)
                putExtra("image", compressedBitmap)
                putExtra("category", category)
                putExtra("img_url", it.img_url.text)
            };



            context.startActivity(intent)

        }
    }

    override fun getItemCount() = itemList.size

    class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.imageView
        val textTitle: TextView = itemView.textTitle
        val textInfo: TextView = itemView.textInfo
        val imgUrl: TextView = itemView.img_url

    }
}

