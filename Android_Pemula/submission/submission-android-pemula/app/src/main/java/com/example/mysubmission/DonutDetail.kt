package com.example.mysubmission

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DonutDetail : AppCompatActivity() {

    companion object {
        const val DATA_DONUT = "DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donut_detail)

        val donut = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Donut>(DATA_DONUT, Donut::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Donut>(DATA_DONUT)
        }

        val imgvDonutPhoto: ImageView = findViewById(R.id.imgv_donut_detail_photo)
        val tvDonutName: TextView = findViewById(R.id.tv_donut_detail_name)
        val tvDonutPrice: TextView = findViewById(R.id.tv_donut_detail_price)
        val tvDonutStock: TextView = findViewById(R.id.tv_donut_detail_stock)
        val tvDonutDesc: TextView = findViewById(R.id.tv_donut_detail_desc)

        val btnShareable: Button = findViewById(R.id.action_share)
        btnShareable.setOnClickListener{
            val url = "https://www.jcodonuts.com/id/en/our-products/donuts"
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", url)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        val imgvPromo: ImageView = findViewById(R.id.imgv_promo)
        imgvPromo.setOnClickListener{
            val url = "https://www.instagram.com/p/C3_wdXVhYpS/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        if (donut != null) {
            imgvDonutPhoto.setImageResource(donut.photo)
            tvDonutName.text = donut.name
            tvDonutPrice.text = donut.price
            tvDonutStock.text = donut.stock
            tvDonutDesc.text = donut.description
        }
    }
}