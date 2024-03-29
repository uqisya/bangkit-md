package com.example.mysubmission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvDonuts: RecyclerView
    private val list = ArrayList<Donut>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvDonuts = findViewById(R.id.rv_donuts)
        rvDonuts.setHasFixedSize(true)

        list.addAll(getListDonuts())
        showRecyclerList()
    }

    private fun getListDonuts(): ArrayList<Donut> {
        val dataDonutName = resources.getStringArray(R.array.data_donut_name)
        val dataDonutPrice = resources.getStringArray(R.array.data_donut_price)
        val dataDonutStock = resources.getStringArray(R.array.data_donut_stock)
        val dataDonutDesc = resources.getStringArray(R.array.data_donut_desc)
        val dataDonutPhoto = resources.obtainTypedArray(R.array.data_donut_photo)

        val listDonuts = ArrayList<Donut>()
        for (item in dataDonutName.indices) {
            val donut = Donut(dataDonutName[item], dataDonutPrice[item], dataDonutStock[item], dataDonutDesc[item], dataDonutPhoto.getResourceId(item, -1))
            listDonuts.add(donut)
        }
        return listDonuts
    }

    private fun showRecyclerList() {
        rvDonuts.layoutManager = LinearLayoutManager(this)
        val listDonutAdapter = ListDonutAdapter(list)
        rvDonuts.adapter = listDonutAdapter

        listDonutAdapter.setOnItemClickCallback(object : ListDonutAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Donut) {
                val goToDetail = Intent(this@MainActivity, DonutDetail::class.java)
                goToDetail.putExtra("DATA", data)
                startActivity(goToDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.about_page) {
//            pindah ke about page
            val aboutIntent = Intent(this, AboutPage::class.java)
            startActivity(aboutIntent)
            true
        } else super.onOptionsItemSelected(item)
    }

}