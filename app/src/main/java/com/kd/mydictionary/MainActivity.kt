package com.kd.mydictionary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import android.app.IntentService
import android.app.Notification
import android.app.AlarmManager
import androidx.core.content.ContextCompat.getSystemService



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val c : Date = Calendar.getInstance().getTime();

    val  NOTIFICATION_REMINDER_NIGHT = 6969


    @SuppressLint("SimpleDateFormat")
    val df = SimpleDateFormat("yyyy-MM-dd");
    val formattedDate = df.format(c);
    var result = Wordoftheday(null , null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(Dispatchers.IO) {
            try {
                val result = getwordoftheday()
                runOnUiThread {
                    tvwordofday.text = result.word
                    tvwordofdaymeaning.text = result.definitions?.get(0)?.text.toString()
                }
            }catch ( e : Exception){
                Log.d("Tag" , e.toString())
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val notifyIntent = Intent(this, MyReceiver::class.java)
        notifyIntent.putExtra("Wordoftheday" , result.word )
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REMINDER_NIGHT,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            (1000 * 60).toLong(), pendingIntent
        )



        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity , SearchActivity::class.java))
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_randomword -> {
                startActivity(Intent(this , RandomActivity::class.java))
            }

            R.id.nav_search_drawer -> {
                 startActivity(Intent(this , SearchActivity::class.java  ))
            }
            R.id.nav_recent -> {
                val i = Intent(this , RecentActivity::class.java  )
                i.putExtra("To" , "Recent")
                startActivity(i)
            }
            R.id.nav_Favourite -> {
                val i = Intent(this , RecentActivity::class.java  )
                i.putExtra("To" , "Favourite")
                startActivity(i)
            }
            R.id.nav_share -> {
                val intent = Intent(android.content.Intent.ACTION_SEND)
                intent.type = "text/plain"
                val shareBodyText = "Your shearing message goes here"
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title")
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText)
                startActivity(Intent.createChooser(intent, "Choose sharing method"))
            }
            R.id.nav_feedback -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    suspend fun getwordoftheday() : Wordoftheday{
        val wordsapi = Retrofitclient.wordsApi
        val key = "wa0lgiwly13awzhdvanwvz562h32dbvzh0own8dsekdamboup"
        val response = wordsapi.getwordoftheday(formattedDate , key)
        Log.d("TAG" , response.body()!!.definitions?.get(0)?.text)

        return if (response.isSuccessful){
            response.body()!!
        }else{
           Wordoftheday(null ,null )
        }
    }
}
