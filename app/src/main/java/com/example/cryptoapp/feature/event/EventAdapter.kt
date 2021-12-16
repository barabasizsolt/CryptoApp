package com.example.cryptoapp.feature.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import com.example.cryptoapp.feature.shared.loadImage

class EventAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) :

    ListAdapter<EventUIModel, EventAdapter.EventViewHolder>(
        object : DiffUtil.ItemCallback<EventUIModel>() {
            override fun areItemsTheSame(oldItem: EventUIModel, newItem: EventUIModel) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: EventUIModel, newItem: EventUIModel) = oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_event, parent, false),
            onItemClickListener = onItemClickListener,
            onItemLongClickListener = onItemLongClickListener
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val uiEventModel = getItem(position)
        val eventOrganizer = "Organizer: " + uiEventModel.organizer
        val startDate = "Starts: " + uiEventModel.startDate
        val endDate = "Ends: " + uiEventModel.endDate
        holder.eventLogo.loadImage(uiEventModel.logo, R.drawable.ic_exchange)
        holder.eventName.text = uiEventModel.title
        holder.eventOrganizer.text = eventOrganizer
        holder.eventStartDate.text = startDate
        holder.eventEndDate.text = endDate
    }

    class EventViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
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
