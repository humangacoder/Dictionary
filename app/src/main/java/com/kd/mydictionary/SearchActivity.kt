package com.kd.mydictionary

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_random.*
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.activity_results.favourites
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_results.*
import kotlinx.android.synthetic.main.content_results.btnwikipedia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val db: RecentDatabase by lazy {
        Room.databaseBuilder(
            this,
            RecentDatabase::class.java,
            "mydb.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbarsearch)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favourites.isVisible = false
        btnwikipedia.isVisible = false

        if (intent.getStringExtra("search") == "recents"){
            etsearch.setText(intent.getStringExtra("tosearch"))
        }


        etsearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */

                    btnsearch.isEnabled = false

                    val progress : ProgressDialog
                    progress = ProgressDialog.show(this@SearchActivity, "Loading",
                        "Please Wait...", true);

                    val recent = Recent(recent = etsearch.text.toString() , isFav = false)
                    db.recentdao().insertrecent(recent)

                    launch {
                        try {

                            val word = etsearch.text.toString()
                            val result = getwordmeaning(word)
                            val example = getexamples(word)
                            val synonym = getsynonym(word)


                            runOnUiThread{

                                if (result.size>0){
                                    val k = result.get(0).word ?: "Not Found!!"
                                    tvword.text =  k
                                    tvpartofspeech.text = result.get(0).partOfSpeech ?: "Not Found!!"
                                    tvmeaning.text = "Meaning : ${result.get(0).text ?: "Not Found!!"}"
                                    tvexample.text = """
                            Example : 
                            ${example.text ?: "Not Found!!".toString()?: "Not Found!!"}
                            """.trimIndent()
                                    favourites.isVisible = true
                                    btnwikipedia.isVisible = true
                                }else{
                                    tvword.text = "NOT FOUND!!!"
                                    tvsynonym.text = ""
                                    tvmeaning.text = ""
                                    tvexample.text = ""
                                }

                                if (synonym.size>=1){
                                    tvsynonym.text = "Synonyms : ${synonym[0].words?.get(0)?: "Not Found!!"} , ${synonym[0].words?.get(1)?: "Not Found!!"}"
                                }
                            }


                        }catch (e : Exception){
                            Log.d("Tag" , e.toString())
                        }
                        progress.dismiss()


                    }
                    btnsearch.isEnabled = true

                    handled = true
                }
                return handled
            }
        })



        btnsearch.setOnClickListener {
            btnsearch.isEnabled = false

            val progress : ProgressDialog
            progress = ProgressDialog.show(this, "Loading",
                "Please Wait...", true);

            val recent = Recent(recent = etsearch.text.toString() , isFav = false)
            db.recentdao().insertrecent(recent)

            launch {
                try {

                    val word = etsearch.text.toString()
                    val result = getwordmeaning(word)
                    val example = getexamples(word)
                    val synonym = getsynonym(word)


                    runOnUiThread{

                       if (result.size>0){
                           val k = result.get(0).word ?: "Not Found!!"
                           tvword.text =  k
                           tvpartofspeech.text = result.get(0).partOfSpeech ?: "Not Found!!"
                           tvmeaning.text = "Meaning : ${result.get(0).text ?: "Not Found!!"}"
                           tvexample.text = """
                            Example : 
                            ${example.text ?: "Not Found!!".toString()?: "Not Found!!"}
                            """.trimIndent()
                           favourites.isVisible = true
                           btnwikipedia.isVisible = true
                       }else{
                           tvword.text = "NOT FOUND!!!"
                           tvsynonym.text = ""
                           tvmeaning.text = ""
                           tvexample.text = ""
                       }

                        if (synonym.size>1){
                            tvsynonym.text = "Synonyms : ${synonym[0].words?.get(0)?: "Not Found!!"} , ${synonym[0].words?.get(1)?: "Not Found!!"}"
                        }
                    }
                    progress.dismiss()

                }catch (e : Exception){
                    Log.d("Tag" , e.toString())
                }


            }
            btnsearch.isEnabled = true
        }

        btnwikipedia.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://en.wikipedia.org/wiki/${etsearch.text}"))
            )
        }

        favourites.setOnClickListener {
          try {
              val recent = Recent(recent = etsearch.text.toString() , isFav = true)
              db.recentdao().insertrecent(recent)
              Toast.makeText(this@SearchActivity, "Favourite Added", Toast.LENGTH_SHORT).show()
          }catch (e : Exception){
              Toast.makeText(this@SearchActivity, "Unale to add favourite..", Toast.LENGTH_SHORT).show()

          }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    suspend fun getwordmeaning(word: String): ArrayList<WordMeaning> {

        val wordapi = Retrofitclient.wordApi

        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"

        val response = wordapi.getwordmeaning(word, 1, false, false, false, key)
        Log.d("Tag", response.body().toString())
        return if (response.isSuccessful) {
            response.body() ?: ArrayList<WordMeaning>()
        } else {
            response.errorBody()
            ArrayList<WordMeaning>()
        }

    }

    suspend fun getexamples(word: String) : Example2{
        val wordapi = Retrofitclient.wordApi

        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"

        val response = wordapi.getexamples(word, false, key)
        Log.d("Tag", response.body().toString())
        return if (response.isSuccessful) {
            response.body() ?: Example2()
        } else {
            response.errorBody()
            Example2()
        }
    }

    suspend fun getsynonym(word: String) : ArrayList<Synonym>{
        val wordapi = Retrofitclient.wordApi

        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"

        val response = wordapi.getrelatedwords(word, false,"synonym",2, key)
        Log.d("Tag", response.body().toString())
        return if (response.isSuccessful) {
            response.body() ?:ArrayList<Synonym>()
        } else {
            ArrayList<Synonym>()
        }
    }



}
