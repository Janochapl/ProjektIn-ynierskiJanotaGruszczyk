package com.example.firebaseprojekt2.functions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView

class DownloadImageTask(bmImage: ImageView): AsyncTask<String, Void, Bitmap?>(){
    var bmImage: ImageView = bmImage
    lateinit var imageURL: String
    override fun doInBackground(vararg urls: String): Bitmap? {
        val tempimageURL = urls[0]
        if(!tempimageURL.contains("http")) imageURL = "https://" + tempimageURL
        else imageURL = tempimageURL
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }
    override fun onPostExecute(result: Bitmap?) {

        bmImage.setImageBitmap(result)
    }

}