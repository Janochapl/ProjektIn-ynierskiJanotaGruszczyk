package com.example.firebaseprojekt2.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseprojekt2.R
import com.example.firebaseprojekt2.data.ProductWithPrice
import com.example.firebaseprojekt2.functions.DownloadImageTask
import kotlinx.android.synthetic.main.recycle_item_prices.view.*
import kotlinx.android.synthetic.main.recycler_item.view.imageView
import kotlinx.android.synthetic.main.recycler_item.view.textInfo
import kotlinx.android.synthetic.main.recycler_item.view.textTitle


class RecyclerAdapterPrices(private val itemList: ProductWithPrice, val context: Context): /*Filterable,*/ RecyclerView.Adapter<RecyclerAdapterPrices.ViewHolder>(){
    var listOfImages: MutableList<Bitmap?> = emptyList<Bitmap?>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_item_prices,
            parent, false
        )
        for (i in 0 until itemList.size){
            listOfImages.add(null)
        }
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = itemList[position]
        if(listOfImages[position]!=null){
            holder.imageView.setImageBitmap(listOfImages[position])
        }else{

            var tempURL: String = currentItem.img_url
            DownloadImageTask(holder.imageView).execute(tempURL)

        }
        holder.textTitle.text = currentItem.product_name
        var tempInfoText = currentItem.p_price + "zł"
        holder.textInfo.text = tempInfoText
        holder.productAddress.text = currentItem.p_url

        holder.itemView.setOnClickListener{
            println(itemList[position].img_url)
            var productAddress: String = if(it.productAdress.text.contains("http")) it.productAdress.text.toString()
            else "https:" + it.productAdress.text
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(productAddress)
            context.startActivity(i)

        }
    }

    override fun getItemCount() = itemList.size

    class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.imageView
        val textTitle: TextView = itemView.textTitle
        val textInfo: TextView = itemView.textInfo
        val productAddress: TextView = itemView.productAdress
    }

}

