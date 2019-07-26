package com.kd.mydictionary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recent_holder.view.*

class RecentAdapter(val recent : ArrayList<Recent>) : RecyclerView.Adapter<RecentAdapter.Viewholder>() {

    var listitemclicklistener : Listitemclicklistener? = null

    fun updateTasks(newrecents: ArrayList<Recent>) {
        recent.clear()
        recent.addAll(newrecents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = li.inflate(R.layout.recent_holder , parent , false)

        return Viewholder(v)
    }

    override fun getItemCount() = recent.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val recent = recent[position]
        holder.bind(recent)

        holder.itemview.setOnClickListener {
            listitemclicklistener?.listitemclicksearch(recent)
        }

        holder.itemview.btndelete.setOnClickListener {
            listitemclicklistener?.lisitemClickdelete(recent)
        }
    }

    inner class Viewholder (val itemview : View) : RecyclerView.ViewHolder(itemview){

        fun bind(recent : Recent){
            itemview.tvholder.text = recent.recent.toString()
        }
    }
}