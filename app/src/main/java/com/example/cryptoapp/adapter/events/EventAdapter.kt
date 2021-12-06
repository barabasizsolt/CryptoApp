package com.example.cryptoapp.adapter.events

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.loadSvg
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.events.Event
import java.util.*

class EventAdapter (private val mList: MutableList<Event>, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
    : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private val mOnItemClickListener: OnItemClickListener = onItemClickListener
    private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_element, parent, false)

        return ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        if(!itemsViewModel.screenshot.isNullOrEmpty()){
            Glide.with(holder.itemView).load(itemsViewModel.screenshot).into(holder.eventLogo)
        }
        if(!itemsViewModel.title.isNullOrEmpty()){
            holder.eventName.text = itemsViewModel.title
        }
        if(!itemsViewModel.organizer.isNullOrEmpty()){
            val text = "Organizer: " + itemsViewModel.organizer
            holder.eventOrganizer.text = text
        }
        if(!itemsViewModel.startDate.isNullOrEmpty()){
            val text = "Starts: " + itemsViewModel.startDate
            holder.eventStartDate.text = text
        }
        if(!itemsViewModel.endDate.isNullOrEmpty()){
            val text = "Ends: " + itemsViewModel.endDate
            holder.eventEndDate.text = text
        }
    }

    override fun getItemCount(): Int = mList.size

    fun addData(data : MutableList<Event>){
        val insertIndex = mList.size
        mList.addAll(insertIndex, data)
        notifyItemRangeInserted(insertIndex, data.size)
    }

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

        private val mOnItemClickListener: OnItemClickListener = onItemClickListener
        private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener

        val eventLogo: ImageView = itemView.findViewById(R.id.event_logo)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventOrganizer: TextView = itemView.findViewById(R.id.event_organizer)
        val eventStartDate: TextView = itemView.findViewById(R.id.event_start_date)
        val eventEndDate: TextView = itemView.findViewById(R.id.event_end_date)

        override fun onClick(view: View) {
            mOnItemClickListener.onItemClick(bindingAdapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            mOnItemLongClickListener.onItemLongClick(bindingAdapterPosition)
            return true
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
}