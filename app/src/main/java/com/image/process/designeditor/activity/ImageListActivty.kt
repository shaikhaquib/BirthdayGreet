package com.image.process.designeditor.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.image.process.R
import com.image.process.model.HeaderResponse
import com.image.process.model.template.TemplateListResponse
import kotlinx.android.synthetic.main.item_template.*

class ImageListActivty : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list_activty)

        recyclerView = findViewById(R.id.rvTemplate)
        getTemplate()
    }

    private fun getTemplate() {

        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.254:8080/icreate/API/BirthdayBaseImageListAPI.php"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
            val gson = Gson()
                val m: TemplateListResponse = gson.fromJson<TemplateListResponse>(
                    it,
                    TemplateListResponse::class.java
                )

                val images = m.birthdayBaseImageList

                recyclerView.adapter = object : RecyclerView.Adapter<ViewHolder>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): ViewHolder {
                        return ViewHolder(layoutInflater.inflate(R.layout.item_template,parent,false))
                    }

                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val viewHolder = holder as ViewHolder
                        viewHolder.fab.setOnClickListener{
                            startActivity(Intent(applicationContext,DesignStudio::class.java).putExtra("image",images[position].birthdayImage))
                        }

                        Glide.with(applicationContext).load(images.get(position).birthdayImage)
                            // .transition(GenericTransitionOptions.with(animationObject))
                            .placeholder(R.color.white)
                            .apply(
                                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(
                                    ObjectKey(0)
                                )                    )
                            .into(viewHolder.template)

                    }

                    override fun getItemCount(): Int {
                        return images.size
                    }
                    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
                        val fab = itemView.findViewById<FloatingActionButton>(R.id.edit)
                        val template = itemView.findViewById<ImageView>(R.id.template)
                    }
                }

            }, {

            })
        queue.add(stringRequest)

// Add the request to the RequestQueue.

    }
}