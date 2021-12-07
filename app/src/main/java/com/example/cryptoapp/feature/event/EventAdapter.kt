package com.example.cryptoapp.feature.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import com.example.cryptoapp.data.model.event.Event

class EventAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
)

    : ListAdapter<Event, EventAdapter.EventViewHolder>(
    object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_element, parent, false),
            onItemClickListener = onItemClickListener,
            onItemLongClickListener = onItemLongClickListener
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val uiEventModel = getItem(position)

        if (!uiEventModel.screenshot.isNullOrEmpty()) {
            Glide.with(holder.itemView).load(uiEventModel.screenshot).into(holder.eventLogo)
        }
        if (!uiEventModel.title.isNullOrEmpty()) {
            holder.eventName.text = uiEventModel.title
        }
        if (!uiEventModel.organizer.isNullOrEmpty()) {
            val text = "Organizer: " + uiEventModel.organizer
            holder.eventOrganizer.text = text
        }
        if (!uiEventModel.startDate.isNullOrEmpty()) {
            val text = "Starts: " + uiEventModel.startDate
            holder.eventStartDate.text = text
        }
        if (!uiEventModel.endDate.isNullOrEmpty()) {
            val text = "Ends: " + uiEventModel.endDate
            holder.eventEndDate.text = text
        }
    }

    class EventViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    )

        : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val eventLogo: ImageView = itemView.findViewById(R.id.event_logo)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventOrganizer: TextView = itemView.findViewById(R.id.event_organizer)
        val eventStartDate: TextView = itemView.findViewById(R.id.event_start_date)
        val eventEndDate: TextView = itemView.findViewById(R.id.event_end_date)

        override fun onClick(view: View) {
            onItemClickListener.onItemClick(bindingAdapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            onItemLongClickListener.onItemLongClick(bindingAdapterPosition)
            return true
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
}