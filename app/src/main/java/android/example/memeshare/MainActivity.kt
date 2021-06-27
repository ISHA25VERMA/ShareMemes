package android.example.memeshare

import android.app.DownloadManager
import android.app.VoiceInteractor
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    var imageURL :String? = null

    private val url = "https://meme-api.herokuapp.com/gimme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadImage()

        button_next.setOnClickListener(){
            LoadImage()
        }
        button_share.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this awesome meme I found from " +
                    "meme share app $imageURL")
            val chooser = Intent.createChooser(intent, "Share using ")
            startActivity(chooser)
        }
    }

    fun LoadImage(){
        progrssBar.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null
            , { response ->
                val preview = response.getJSONArray("preview")
                val l = preview.length();
                imageURL = preview.getString(l-1)
                Glide.with(this).load(imageURL).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progrssBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progrssBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImage);

                Log.d("myapp", "ok done")
            }, {
                Log.d("myapp", "error in parsing" )
            });
        val queue = SingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}