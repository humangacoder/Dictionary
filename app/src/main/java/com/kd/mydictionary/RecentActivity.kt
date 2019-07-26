package com.kd.mydictionary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_recent.*
import kotlinx.android.synthetic.main.recent_holder.view.*

class RecentActivity : AppCompatActivity() {

    val db: RecentDatabase by lazy {
        Room.databaseBuilder(
            this,
            RecentDatabase::class.java,
            "mydb.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)

        setSupportActionBar(toolbarrecent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        var list = ArrayList<Recent>()
        val recentAdapter = RecentAdapter(list)

        rvholder_recent.layoutManager = LinearLayoutManager(this@RecentActivity , RecyclerView.VERTICAL , false)
        rvholder_recent.adapter = recentAdapter


        if (intent.getStringExtra("To") == "Recent"){
            supportActionBar?.title = "Recent"

            db.recentdao().getAllrecent().observe(this , Observer {
                list = it as ArrayList<Recent>
                recentAdapter.updateTasks(list)
            })

            recentAdapter.listitemclicklistener = object : Listitemclicklistener {

                override fun listitemclicksearch(recent: Recent) {
                    val i = Intent(this@RecentActivity , SearchActivity::class.java)
                    i.putExtra("search" , "recents")
                    i.putExtra("tosearch" , recent.recent)
                    startActivity(i)
                }

                override fun lisitemClickdelete(recent: Recent) {
                    db.recentdao().deleterecent(recent)
                }
            }




        }else{
            supportActionBar?.title = "Favourites"

            db.recentdao().getAllfav().observe(this , Observer {
                list = it as ArrayList<Recent>
                recentAdapter.updateTasks(list)
            })

            recentAdapter.listitemclicklistener = object : Listitemclicklistener {
                override fun listitemclicksearch(recent: Recent) {
                    val i = Intent(this@RecentActivity , SearchActivity::class.java)
                    i.putExtra("search" , "recents")
                    i.putExtra("tosearch" , recent.recent)
                    startActivity(i)
                }

                override fun lisitemClickdelete(recent: Recent) {
                    db.recentdao().deleterecent(recent)
                }
            }



        }





    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.getItemId()) {
//
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.nav_search_main -> {

                startActivity(Intent(this , SearchActivity::class.java))

                true
            }

            R.id.btndeleteall_menu -> {
                if (supportActionBar?.title == "Recent"){
                    db.recentdao().deleteallrecent()
                }else{
                    db.recentdao().deleteallFav()
                }

                true
            }

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


