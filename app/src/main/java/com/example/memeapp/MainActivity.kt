package com.example.memeapp

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var memeUrl : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()



    }
    private fun loadmeme(){
        //whem meme loading its show progress bar
        val loding : ProgressBar = findViewById(R.id.progress)
        loding.visibility = View.VISIBLE

        // when a new meme load
        val queue = Volley.newRequestQueue(this)
        val url= "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { it ->
                memeUrl= it.getString("url")
                var image:ImageView =findViewById(R.id.memeimage)


                Glide.with(this).load(memeUrl).listener(object : RequestListener<Drawable> {
                    //progress bar function
                    override fun onLoadFailed( //when meme failed to load show progress bar
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loding.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady( // when meme loaded remove progress bar
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loding.visibility = View.GONE
                        return false
                    }

                }).into(image)



            },
            Response.ErrorListener {


            }
        )
        queue.add(jsonObjectRequest)





    }

    fun shareMeme(view: android.view.View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type ="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"CHECK THIS MEME")

        //CREATE CHOOSER TO SELECT FROM EHIVH APP HE WANT TO SEND
        val chooser = Intent.createChooser(intent,"share meme using__")
        startActivity(chooser)
    }
    fun NextMeme(view: android.view.View) {
        //for next meme just call api once again
        loadmeme()


    }

    fun downloadmeme(view: android.view.View) {
        //to download our image
        val request= DownloadManager.Request(Uri.parse(memeUrl))
            .setTitle("Downloading__")
            .setDescription("Debjits meme")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)

        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)


    }
}