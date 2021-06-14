package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentUrl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
        val nextBtn :Button = findViewById(R.id.NextBtn)
        nextBtn.setOnClickListener {
            loadMeme()
        }

        val shareBtn :Button = findViewById(R.id.shareBtn)
        shareBtn.setOnClickListener {
            val intent  = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "See this meme - $currentUrl" )
            val chooser = Intent.createChooser(intent , "Share From : ")
            startActivity(chooser)
        }
    }

    private fun loadMeme() {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                currentUrl = it.get("url").toString()
                val memeImg :ImageView = findViewById(R.id.meme)

                Glide.with(this).load(currentUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility= View.GONE
                        return false
                    }
                }).into(memeImg)
            },
            Response.ErrorListener {
                Toast.makeText(this , "Internet Connection" , Toast.LENGTH_SHORT).show()
            }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}

