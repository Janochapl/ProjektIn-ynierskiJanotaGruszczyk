package com.example.firebaseprojekt2.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.set
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseprojekt2.data.DatabaseProduct
import com.example.firebaseprojekt2.ProductPrices
import com.example.firebaseprojekt2.R
import com.example.firebaseprojekt2.functions.DownloadImageTask
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.io.ByteArrayOutputStream

class RecyclerAdapterDatabase(private val itemList: DatabaseProduct, val context: Context): /*Filterable,*/ RecyclerView.Adapter<RecyclerAdapterDatabase.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item,
        parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = itemList[position]
        DownloadImageTask(holder.imageView).execute(currentItem.img_url)
        holder.textTitle.text = currentItem.product_name
        var tempInfoText = currentItem.info_text
        tempInfoText =  tempInfoText.replace(';', '\n')
        holder.textInfo.text = tempInfoText
        holder.imageView.isClickable
        holder.category.text = currentItem.category
        holder.img_url.text = currentItem.img_url

        holder.imageView.setOnClickListener{
            println(holder.adapterPosition)
        }

        holder.itemView.setOnClickListener{
            var compressedBitmap: Bitmap
            try{
                compressedBitmap = it.imageView.drawToBitmap()
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
            }catch (e: Exception){
                print(e)
                val conf = Bitmap.Config.ARGB_8888
                compressedBitmap= Bitmap.createBitmap(1,1, conf)
            }

            val intent = Intent(context, ProductPrices::class.java).apply {

                putExtra("product_name", it.textTitle.text)
                putExtra("info_text", it.textInfo.text)
                putExtra("image", compressedBitmap)
                putExtra("category", it.category.text)
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
        val category: TextView = itemView.category
        val img_url: TextView = itemView.img_url

    }

}

