package com.kd.mydictionary

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        setSupportActionBar(toolbarresults)
        supportActionBar?.title = "WORD"   ////Remember to display the name of th word whose meaning has been searched
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        favourites.setOnClickListener { view ->
            Snackbar.make(view, "Favourite Added!!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

}
