package com.kd.mydictionary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val word = intent.getStringExtra("Word")
        val intent1 = Intent(context, MyNewIntentService::class.java)
        intent1.putExtra("Word" , word)
        context.startService(intent1)
    }
}