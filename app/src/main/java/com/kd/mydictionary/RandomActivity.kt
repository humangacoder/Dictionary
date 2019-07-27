package com.kd.mydictionary

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_random.*
import kotlinx.android.synthetic.main.activity_random.btnwikipedia
import kotlinx.android.synthetic.main.activity_random.favourites
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_results.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import android.view.inputmethod.EditorInfo
import android.widget.TextView



class RandomActivity : AppCompatActivity(), CoroutineScope {
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
        setContentView(R.layout.activity_random)

        setSupportActionBar(toolbarrandom)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "RandomWords"

        var randomWord = RandomWord(null, null)

        favourites.isVisible = false
        btnwikipedia.isVisible = false



        btngenerate.setOnClickListener {
            val progress : ProgressDialog
            progress = ProgressDialog.show(this, "Loading",
                "Please Wait...", true);

                launch {
                    try {

                        randomWord = getrandomword()
                        Log.d("Tag", "word : ${randomWord.word}")


                        val word = randomWord.word!!
                        val result = getwordmeaning(word)
                        val example = getexamples(word)
                        var synonym = ArrayList<Synonym>()
                        synonym = getsynonym(word)

                        Log.d("Tag", result.get(0).toString())
                        Log.d("Tag", example.text.toString())

                        Log.d("Tag", synonym.size.toString())


                        runOnUiThread {
                            if (result.size > 0 && randomWord.word != "null") {
                                tvwordrandom.text = randomWord.word
                                tvpartofspeechrandom.text = result.get(0).partOfSpeech
                                tvmeaningrandom.text = "Meaning : ${result.get(0).text}"
                                tvexamplerandom.text = """
                            Example :
                            ${example.text ?: "Not Found!!".toString() ?: "Not Found!!"}
                        """.trimIndent()
                                favourites.isVisible = true
                                btnwikipedia.isVisible = true
                            } else {
                                tvwordrandom.text = "NOT FOUND!!!"
                                tvmeaningrandom.text = "Please return after a few minutes :)"
                                tvexamplerandom.text = " That's all for the time being..."
                                tvsynonymrandom.text = ""
                            }

                            if (synonym.size >= 1) {
                                tvsynonymrandom.text =
                                    "Synonyms : ${synonym[0].words?.get(0)} , ${synonym[0].words?.get(1)}"
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("Tag", e.toString())
                        tvwordrandom.text = "NOT FOUND!!!"
                        tvmeaningrandom.text = "Please return after an hour or so :)"
                        tvexamplerandom.text = " That's all for the time being..."
                        tvsynonymrandom.text = ""
                    }
                    progress.dismiss()

                }

        }
        favourites.setOnClickListener {
            val recent = Recent(recent = randomWord.word!!, isFav = true)
            db.recentdao().insertrecent(recent)
            Toast.makeText(this@RandomActivity, "Favourite Added", Toast.LENGTH_SHORT).show()
        }
        btnwikipedia.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://en.wikipedia.org/wiki/${randomWord.word}"))
            )
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

    suspend fun getrandomword(): RandomWord {

        val wordapi = Retrofitclient.wordsApi
        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"
        val response = wordapi.getrandomwords(true, -1, 1, -1, 5, -1, key)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            RandomWord(null, null)
        }
    }

    suspend fun getwordmeaning(word: String): ArrayList<WordMeaning> {

        val wordapi = Retrofitclient.wordApi

        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"

        val response = wordapi.getwordmeaning(word, 5, false, false, false, key)
        Log.d("Tag", response.body()!!.toString())
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            ArrayList<WordMeaning>()
        }

    }

    suspend fun getexamples(word: String): Example2 {
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

    suspend fun getsynonym(word: String): ArrayList<Synonym> {
        val wordapi = Retrofitclient.wordApi

        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"

        val response = wordapi.getrelatedwords(word, false, "synonym", 3, key)
        Log.d("Tag", response.body().toString())
        return if (response.isSuccessful) {
            response.body() ?: ArrayList<Synonym>()
        } else {
            ArrayList<Synonym>()
        }
    }
}
