package com.example.cryptoapp.adapter.exchanges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.constant.coingekko.ExchangeConstant.loadPng
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setCompactPrice
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.exchanges.Exchange

class ExchangeAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
)

    : ListAdapter<Exchange, ExchangeAdapter.ExchangeViewHolder>(
    object : DiffUtil.ItemCallback<Exchange>() {
        override fun areItemsTheSame(oldItem: Exchange, newItem: Exchange) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Exchange, newItem: Exchange) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder =
        ExchangeViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.exchange_element, parent, false),
            onItemClickListener = onItemClickListener,
            onItemLongClickListener = onItemLongClickListener
        )

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val itemsViewModel = getItem(position)

        if (!itemsViewModel.image.isNullOrEmpty()) {
            holder.exchangeLogo.loadPng(itemsViewModel.image)
        }
        if (!itemsViewModel.name.isNullOrEmpty()) {
            holder.exchangeName.text = itemsViewModel.name
        }
        if (itemsViewModel.trust_score != null) {
            holder.exchangeTrustScore.text = itemsViewModel.trust_score.toString()
        }
        if (itemsViewModel.trade_volume_24h_btc != null) {
            holder.volume.text = setCompactPrice(itemsViewModel.trade_volume_24h_btc)
        }
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