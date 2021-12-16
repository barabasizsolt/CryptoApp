package com.example.cryptoapp.feature.exchange

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
import com.example.cryptoapp.feature.shared.convertToCompactPrice
import com.example.cryptoapp.feature.shared.loadImage

class ExchangeAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) :
    ListAdapter<ExchangeUIModel, ExchangeAdapter.ExchangeViewHolder>(
        object : DiffUtil.ItemCallback<ExchangeUIModel>() {
            override fun areItemsTheSame(oldItem: ExchangeUIModel, newItem: ExchangeUIModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ExchangeUIModel, newItem: ExchangeUIModel) = oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder =
        ExchangeViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_exchange_exchange, parent, false),
            onItemClickListener = onItemClickListener,
            onItemLongClickListener = onItemLongClickListener
        )

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val uiExchangeModel = getItem(position)
        holder.exchangeLogo.loadImage(uiExchangeModel.logo, R.drawable.ic_bitcoin)
        holder.exchangeName.text = uiExchangeModel.name
        holder.exchangeTrustScore.text = uiExchangeModel.trustScore
        holder.volume.text = uiExchangeModel.volume.convertToCompactPrice()
    }

    class ExchangeViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val exchangeLogo: ImageView = itemView.findViewById(R.id.exchange_logo)
        val exchangeName: TextView = itemView.findViewById(R.id.exchange_name)
        val exchangeTrustScore: TextView = itemView.findViewById(R.id.exchange_trust_score)
        val volume: TextView = itemView.findViewById(R.id.volume)

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
